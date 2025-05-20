package com.cs3332.handler.order;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.Role;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.order.OrderInfoPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.TextResponse;
import com.cs3332.core.utils.Response;
import com.cs3332.data.object.order.Order;
import com.cs3332.handler.constructor.AbstractBodyHandler;

public class UpdateOrdersInfoHandler extends AbstractBodyHandler<OrderInfoPayload> {
    private String token;

    public UpdateOrdersInfoHandler(Server server) {
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

        order = new Order(
                payload.getTableID(),
                payload.getOrderID(),
                payload.getItems(),
                payload.getOrderTimestamp(),
                payload.getStatus(),
                payload.getUserID(),
                payload.getPaymentTimestamp(),
                payload.getPreparationStartTimestamp(),
                payload.getReadyTimestamp(),
                payload.getPreparedBy()
        );

        // Validate status transition
        /*if (!isValidStatusTransition(order.getStatus(), payload.getNewStatus())) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, 
                new ErrorResponse("Invalid status transition from " + order.getStatus() + " to " + payload.getNewStatus()));
        }*/

        // Update status and relevant timestamps

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
} 