package com.cs3332.handler.order;

import com.cs3332.Server;
import com.cs3332.core.object.Param;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.order.OrderResponse;
import com.cs3332.data.object.order.Order;
import com.cs3332.data.object.order.OrderItem;
import com.cs3332.handler.constructor.AbstractHandler;

import java.util.UUID;

public class GetOrderInfoHandler extends AbstractHandler {
    private String token;
    @Param
    private UUID orderID;

    public GetOrderInfoHandler(Server server) {
        super(server, RequestMethod.GET);
    }

    @Override
    protected ServerResponse resolve() {
        if (orderID == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Order ID is required."));
        }

        Order order = server.getDataManager().getProductionDBSource().getOrder(orderID);
        if (order == null) {
            return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse("Order not found."));
        }

        OrderResponse response = new OrderResponse(
               order,
                server.getDataManager().getAuthenticationSource()
        );

        return new ServerResponse(ResponseCode.FOUND, response);
    }
} 