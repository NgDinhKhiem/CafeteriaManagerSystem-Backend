package com.cs3332.handler.order;

import com.cs3332.Server;
import com.cs3332.core.object.*;
import com.cs3332.core.payload.object.order.UpdateOrderStatusPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.TextResponse;
import com.cs3332.core.utils.Response;
import com.cs3332.data.object.order.Order;
import com.cs3332.data.object.order.OrderStatus;
import com.cs3332.handler.constructor.AbstractBodyHandler;

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
        if (payload.getNewStatus() == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("New status is required."));
        }

        Order order = server.getDataManager().getProductionDBSource().getOrder(payload.getOrderID());
        if (order == null) {
            return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse("Order not found."));
        }

        // Validate status transition
        if (!isValidStatusTransition(order.getStatus(), payload.getNewStatus())) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, 
                new ErrorResponse("Invalid status transition from " + order.getStatus() + " to " + payload.getNewStatus()));
        }

        // Update status and relevant timestamps
        Long paymentTimestamp = null;
        Long preparationStartTimestamp = null;
        Long readyTimestamp = null;
        Long completionTimestamp = null;
        String preparedBy = null;

        switch (payload.getNewStatus()) {
            case PAID:
                paymentTimestamp = System.currentTimeMillis();
                break;
            case PREPARING:
                preparationStartTimestamp = System.currentTimeMillis();
                preparedBy = dataManager.getAccountInformation(token).getUsername();
                break;
            case READY:
                readyTimestamp = System.currentTimeMillis();
                break;
            case COMPLETED:
                completionTimestamp = System.currentTimeMillis();
                break;
        }

        // Update order with new status and timestamps
        Response response = server.getDataManager().getProductionDBSource().updateOrderStatus(
                payload.getOrderID(),
                payload.getNewStatus(),
                paymentTimestamp
        );

        if (response.getState()) {
            // Update additional timestamps if needed
            if (preparationStartTimestamp != null) {
                order.setPreparationStartTimestamp(preparationStartTimestamp);
            }
            if (readyTimestamp != null) {
                order.setReadyTimestamp(readyTimestamp);
            }
            if (completionTimestamp != null) {
                order.setCompletionTimestamp(completionTimestamp);
            }
            if (preparedBy != null) {
                order.setPreparedBy(preparedBy);
            }
            server.getDataManager().getProductionDBSource().save();
            return new ServerResponse(ResponseCode.OK, new TextResponse("Order status updated successfully."));
        } else {
            return new ServerResponse(ResponseCode.INTERNAL_SERVER_ERROR, new ErrorResponse(response.getResponse()));
        }
    }

    private boolean isValidStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (currentStatus == OrderStatus.CANCELLED) {
            return false; // Cannot change status of cancelled orders
        }
        if (currentStatus == OrderStatus.COMPLETED) {
            return false; // Cannot change status of completed orders
        }

        switch (currentStatus) {
            case PENDING_PAYMENT:
                return newStatus == OrderStatus.PAID || newStatus == OrderStatus.CANCELLED;
            case PAID:
                return newStatus == OrderStatus.PREPARING || newStatus == OrderStatus.CANCELLED;
            case PREPARING:
                return newStatus == OrderStatus.READY || newStatus == OrderStatus.CANCELLED;
            case READY:
                return newStatus == OrderStatus.COMPLETED || newStatus == OrderStatus.CANCELLED;
            default:
                return false;
        }
    }
} 