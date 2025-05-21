package com.cs3332.handler.order;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.Role;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.order.CreateOrderPayload;
import com.cs3332.core.payload.object.order.OrderItemPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.order.OrderResponse;
import com.cs3332.core.utils.Utils;
import com.cs3332.data.object.auth.UserInformation;
import com.cs3332.data.object.order.Order;
import com.cs3332.data.object.order.OrderItem;
import com.cs3332.data.object.order.OrderStatus;
import com.cs3332.data.object.storage.Ingredient;
import com.cs3332.data.object.storage.Item;
import com.cs3332.data.object.storage.Product;
import com.cs3332.handler.constructor.AbstractBodyHandler;

import java.util.*;

public class CreateOrderHandler extends AbstractBodyHandler<CreateOrderPayload> {
    private String token;

    public CreateOrderHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        UserInformation requester = dataManager.getAccountInformation(token);
        if (requester == null) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("Invalid token."));
        }

        List<Role> roles = dataManager.getRole(token);
        if (!roles.contains(Role.CASHIER) && !roles.contains(Role.BARTENDER) && !roles.contains(Role.MANAGER) && !roles.contains(Role.ADMIN)) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("You do not have permission to create an order."));
        }

        if (payload.getItems() == null || payload.getItems().isEmpty()) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Order must contain at least one item."));
        }

        List<OrderItem> orderItems = new ArrayList<>();
        
        // Check ingredient availability for all products in the order
        Map<UUID, Float> requiredIngredients = new HashMap<>();
        
        for (OrderItemPayload itemPayload : payload.getItems()) {
            if (itemPayload.getProductID() == null || itemPayload.getQuantity() <= 0) {
                return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Invalid item details: Product ID is null or quantity is not positive."));
            }
            
            Product product = server.getDataManager().getProductionDBSource().getProduct(itemPayload.getProductID());
            if (product == null) {
                return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse("Product not found: " + itemPayload.getProductID()));
            }
            
            // Calculate required ingredients for this product
            for (Ingredient ingredient : product.getRecipe()) {
                UUID itemStackId = ingredient.getItemStackID();
                float requiredQuantity = ingredient.getQuantity() * itemPayload.getQuantity();
                
                // Add to our running total of required ingredients
                requiredIngredients.put(
                    itemStackId,
                    requiredIngredients.getOrDefault(itemStackId, 0f) + requiredQuantity
                );
            }

            OrderItem orderItem = new OrderItem(product.getID(), itemPayload.getQuantity(), product.getPrice());
            orderItems.add(orderItem);
        }
        
        // Verify we have enough of each ingredient in inventory
        for (Map.Entry<UUID, Float> entry : requiredIngredients.entrySet()) {
            UUID itemStackId = entry.getKey();
            float requiredAmount = entry.getValue();
            
            // Calculate available amount from inventory
            float availableAmount = 0f;
            for (Item item : server.getDataManager().getProductionDBSource().getAllItem()) {
                if (item.getItemStackID().equals(itemStackId)) {
                    availableAmount += item.getQuantity();
                }
            }
            
            if (availableAmount < requiredAmount) {
                // Get the item stack name for a better error message
                String itemName = "Unknown";
                var itemStack = server.getDataManager().getProductionDBSource().getItemStack(itemStackId);
                if (itemStack != null) {
                    itemName = itemStack.getName();
                }
                
                return new ServerResponse(
                    ResponseCode.BAD_REQUEST, 
                    new ErrorResponse("Insufficient inventory for ingredient: " + itemName + 
                                     ". Required: " + requiredAmount + ", Available: " + availableAmount)
                );
            }
        }

        Order newOrder = new Order(
                payload.getTableID(),
                UUID.randomUUID(),
                orderItems,
                Utils.getTime(),
                OrderStatus.PENDING_CONFIRMATION,
                requester.getUsername(),
                0L,
                0L,
                0L,
                ""
        );

        Order createdOrder = server.getDataManager().getProductionDBSource().createOrder(newOrder);
        if (createdOrder == null) {
            return new ServerResponse(ResponseCode.INTERNAL_SERVER_ERROR, new ErrorResponse("Failed to create order. Please try again."));
        }

        OrderResponse response = new OrderResponse(
                createdOrder
        );

        return new ServerResponse(ResponseCode.CREATED, response);
    }
} 