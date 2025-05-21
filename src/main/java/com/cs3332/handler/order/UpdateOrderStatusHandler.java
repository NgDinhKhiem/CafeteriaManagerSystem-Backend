package com.cs3332.handler.order;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.Role;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.order.UpdateOrderStatusPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.TextResponse;
import com.cs3332.core.utils.Response;
import com.cs3332.core.utils.Utils;
import com.cs3332.data.object.order.Order;
import com.cs3332.data.object.order.OrderItem;
import com.cs3332.data.object.order.OrderStatus;
import com.cs3332.data.object.storage.Ingredient;
import com.cs3332.data.object.storage.Item;
import com.cs3332.data.object.storage.Product;
import com.cs3332.handler.constructor.AbstractBodyHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UpdateOrderStatusHandler extends AbstractBodyHandler<UpdateOrderStatusPayload> {
    private String token;

    public UpdateOrderStatusHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        // Check authorization
        if (!dataManager.getRole(token).contains(Role.CASHIER) && 
            !dataManager.getRole(token).contains(Role.BARTENDER) &&
            !dataManager.getRole(token).contains(Role.MANAGER) &&
            !dataManager.getRole(token).contains(Role.ADMIN)) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, 
                new ErrorResponse("You do not have permission to update order status."));
        }

        if (payload.getOrderID() == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Order ID is required."));
        }

        Order order = server.getDataManager().getProductionDBSource().getOrder(payload.getOrderID());
        if (order == null) {
            return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse("Order not found."));
        }

        // Check if we're transitioning to CONFIRMED status
        boolean isConfirming = payload.getNewStatus() == OrderStatus.READY;

        // Set the new status
        order.setStatus(payload.getNewStatus());
        
        // If we're confirming the order, deduct from inventory
        if (isConfirming) {
            ServerResponse deductionResponse = deductIngredientsFromInventory(order);
            if (deductionResponse != null) {
                return deductionResponse;
            }
        }

        // Update order with new status and timestamps
        Response response = server.getDataManager().getProductionDBSource().updateOrderInformation(
                payload.getOrderID(),
                order
        );

        if (response.getState()) {
            return new ServerResponse(ResponseCode.OK, new TextResponse("Order status updated successfully."));
        } else {
            return new ServerResponse(ResponseCode.INTERNAL_SERVER_ERROR, new ErrorResponse(response.getResponse()));
        }
    }
    
    /**
     * Deducts ingredients from inventory based on order items
     * @param order The order to process
     * @return An error response if the deduction fails, null if successful
     */
    private ServerResponse deductIngredientsFromInventory(Order order) {
        // Calculate required ingredients from all order items
        Map<UUID, Float> requiredIngredients = new HashMap<>();
        
        for (OrderItem orderItem : order.getItems()) {
            Product product = server.getDataManager().getProductionDBSource().getProduct(orderItem.getProductID());
            if (product == null) {
                return new ServerResponse(ResponseCode.INTERNAL_SERVER_ERROR, 
                    new ErrorResponse("Product not found: " + orderItem.getProductID()));
            }
            
            // Calculate required ingredients
            for (Ingredient ingredient : product.getRecipe()) {
                UUID itemStackId = ingredient.getItemStackID();
                float requiredQuantity = ingredient.getQuantity() * orderItem.getQuantity();
                
                // Add to total required ingredients
                requiredIngredients.put(
                    itemStackId, 
                    requiredIngredients.getOrDefault(itemStackId, 0f) + requiredQuantity
                );
            }
        }
        
        // Now, deduct ingredients from inventory (FIFO - oldest first)
        for (Map.Entry<UUID, Float> entry : requiredIngredients.entrySet()) {
            UUID itemStackId = entry.getKey();
            float amountToDeduct = entry.getValue();
            
            // Get all items of this type, sorted by expiration date (oldest first)
            List<Item> items = server.getDataManager().getProductionDBSource().getAllItem();
            items.removeIf(item -> !item.getItemStackID().equals(itemStackId));
            items.sort((a, b) -> Long.compare(a.getExpiration_date(), b.getExpiration_date()));
            
            // Deduct from each item until we've deducted enough
            for (Item item : items) {
                if (amountToDeduct <= 0) break;
                
                if (item.getQuantity() <= amountToDeduct) {
                    // Use up entire item
                    amountToDeduct -= item.getQuantity();
                    
                    // Remove the item (it's depleted)
                    Response deleteResponse = server.getDataManager().getProductionDBSource().deleteIOItem(item.getEntryID());
                    if (!deleteResponse.getState()) {
                        return new ServerResponse(ResponseCode.INTERNAL_SERVER_ERROR, 
                            new ErrorResponse("Failed to remove used item: " + deleteResponse.getResponse()));
                    }
                } else {
                    // Create new item with reduced quantity
                    Item updatedItem = new Item(
                        item.getItemStackID(),
                        item.getEntryID(),
                        item.getImportExportDate(),
                        item.getExpiration_date(),
                            - amountToDeduct,
                        item.getSupplier(),
                            item.getReason()
                    );
                    
                    // Add updated item
                    Response importResponse = server.getDataManager().getProductionDBSource().exportItem(updatedItem);
                    if (!importResponse.getState()) {
                        return new ServerResponse(ResponseCode.INTERNAL_SERVER_ERROR, 
                            new ErrorResponse("Failed to update item: " + importResponse.getResponse()));
                    }
                    break;
                }
            }
            
            /*if (amountToDeduct > 0) {
                String itemName = "Unknown";
                var itemStack = server.getDataManager().getProductionDBSource().getItemStack(itemStackId);
                if (itemStack != null) {
                    itemName = itemStack.getName();
                }
                
                return new ServerResponse(ResponseCode.INTERNAL_SERVER_ERROR, 
                    new ErrorResponse("Insufficient inventory for " + itemName + " when confirming order."));
            }*/
        }
        
        return null; // Success
    }
} 