package com.cs3332.handler.order;

import com.cs3332.Server;
import com.cs3332.core.object.*;
import com.cs3332.core.payload.object.order.OrderInfoPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.order.OrderResponse;
import com.cs3332.data.object.order.Order;
import com.cs3332.handler.constructor.AbstractBodyHandler;

import java.util.UUID;

public class GetOrderInfoHandler extends AbstractBodyHandler<OrderInfoPayload> {
    private String token;

    public GetOrderInfoHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        if (payload.getOrderID() == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Order ID is required."));
        }

        Order order = server.getDataManager().getProductionDBSource().getOrder(payload.getOrderID());
        if (order == null) {
            return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse("Order not found."));
        }

        OrderResponse response = new OrderResponse(
                order.getOrderID(),
                order.getItems(),
                order.getTotalAmount(),
                order.getOrderTimestamp(),
                order.getStatus(),
                order.getUserID(),
                order.getPaymentTimestamp(),
                order.getPreparationStartTimestamp(),
                order.getReadyTimestamp(),
                order.getCompletionTimestamp(),
                order.getNotes(),
                order.getPreparedBy()
        );

        return new ServerResponse(ResponseCode.FOUND, response);
    }
} 