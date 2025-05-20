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
import com.cs3332.data.object.storage.Product;
import com.cs3332.handler.constructor.AbstractBodyHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        // Authorize CASHIER or BARTENDER roles (also allowing ADMIN/MANAGER for testing/override)
        List<Role> roles = dataManager.getRole(token);
        if (!roles.contains(Role.CASHIER) && !roles.contains(Role.BARTENDER) && !roles.contains(Role.MANAGER) && !roles.contains(Role.ADMIN)) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("You do not have permission to create an order."));
        }

        if (payload.getItems() == null || payload.getItems().isEmpty()) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Order must contain at least one item."));
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemPayload itemPayload : payload.getItems()) {
            if (itemPayload.getProductID() == null || itemPayload.getQuantity() <= 0) {
                return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Invalid item details: Product ID is null or quantity is not positive."));
            }
            Product product = server.getDataManager().getProductionDBSource().getProduct(itemPayload.getProductID());
            if (product == null) {
                return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse("Product not found: " + itemPayload.getProductID()));
            }

            OrderItem orderItem = new OrderItem(product.getID(), itemPayload.getQuantity(), product.getPrice());
            orderItems.add(orderItem);
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
                createdOrder.getTableID(),
                createdOrder.getOrderID(),
                createdOrder.getItems(),
                createdOrder.getOrderTimestamp(),
                createdOrder.getStatus(),
                createdOrder.getUserID(),
                createdOrder.getPaymentTimestamp(),
                createdOrder.getPreparationStartTimestamp(),
                createdOrder.getReadyTimestamp(),
                createdOrder.getPreparedBy()
        );

        return new ServerResponse(ResponseCode.CREATED, response);
    }
} 