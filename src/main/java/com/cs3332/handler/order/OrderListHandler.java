package com.cs3332.handler.order;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.Role;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.order.OrderListResponse;
import com.cs3332.handler.constructor.AbstractHandler;

public class OrderListHandler extends AbstractHandler {
    private String token;

    public OrderListHandler(Server server) {
        super(server, RequestMethod.GET);
    }

    @Override
    protected ServerResponse resolve() {
        if (!dataManager.getRole(token).contains(Role.ADMIN) && 
            !dataManager.getRole(token).contains(Role.MANAGER) &&
            !dataManager.getRole(token).contains(Role.BARTENDER) &&
            !dataManager.getRole(token).contains(Role.CASHIER)) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, 
                new ErrorResponse("You do not have permission to view orders."));
        }

        return new ServerResponse(ResponseCode.OK, 
            new OrderListResponse(dataManager.getOrders()));
    }
} 