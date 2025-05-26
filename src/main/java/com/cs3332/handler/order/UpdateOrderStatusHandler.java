package com.cs3332.handler.order;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.Role;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.order.UpdateOrderStatusPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.TextResponse;
import com.cs3332.core.utils.Logger;
import com.cs3332.core.utils.Response;
import com.cs3332.core.utils.Utils;
import com.cs3332.data.object.auth.UserInformation;
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
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Order ID is required. " +payload.toJSON()));
        }

        if (payload.getNewStatus() == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Status is Invalid. " +payload.toJSON()));
        }

        Order order = server.getDataManager().getProductionDBSource().getOrder(payload.getOrderID());

        UserInformation userInformation = dataManager.getAccountInformation(token);
        if(userInformation == null) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED,
                    new ErrorResponse("You do not have permission to update order status."));
        }

        if (order == null) {
            return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse("Order not found."));
        }

        if(this.payload.getNewStatus()==OrderStatus.PAID&&order.getStatus()==OrderStatus.PREPARING){
            if(!order.getPreparedBy().equals(userInformation.getUsername())){
                return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("Not allowed"));
            }
        }

        if ((this.payload.getNewStatus().getWeight()-order.getStatus().getWeight()!=0
                &&this.payload.getNewStatus().getWeight()-order.getStatus().getWeight()!=1)
            ||this.payload.getNewStatus().equals(order.getStatus())) {
            return new ServerResponse(ResponseCode.NOT_MODIFIED, new ErrorResponse("New status weight is invalid!"));
        }

        // Check if we're transitioning to CONFIRMED status
        boolean isConfirming = payload.getNewStatus() == OrderStatus.READY;
        if(order.getStatus()==OrderStatus.PENDING_CONFIRMATION){
            order.setUserID(userInformation.getUsername());
        }

        // Set the new status
        order.setStatus(payload.getNewStatus());

        switch (order.getStatus()){
            case READY:
            case PREPARING:
                order.setPreparedBy(userInformation.getUsername());
                break;
        }

        // If we're confirming the order, deduct from inventory
        if (isConfirming) {
            ServerResponse deductionResponse = deductIngredientsFromInventory(order);
            if (deductionResponse != null) {
                Logger.warn("ERROR IN DEDUCTION: " + deductionResponse.getResponse());
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

                Item updatedItem = new Item(
                        ingredient.getItemStackID(),
                        UUID.randomUUID(),
                        Utils.getTime(),
                        -1,
                        - ingredient.getQuantity(),
                        "Bartender",
                        "Used"
                );

                // Add updated item
                Response importResponse = server.getDataManager().getProductionDBSource().exportItem(updatedItem);
                if (!importResponse.getState()) {
                    return new ServerResponse(ResponseCode.INTERNAL_SERVER_ERROR,
                            new ErrorResponse("Failed to update item: " + importResponse.getResponse()));
                }
            }
        }
        return null; // Success
    }
} 