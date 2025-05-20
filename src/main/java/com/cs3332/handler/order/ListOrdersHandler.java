package com.cs3332.handler.order;

import com.cs3332.Server;
import com.cs3332.core.object.*;
import com.cs3332.core.payload.object.order.ListOrdersPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.order.OrderListResponse;
import com.cs3332.core.response.object.order.OrderResponse;
import com.cs3332.data.object.auth.UserInformation;
import com.cs3332.data.object.order.Order;
import com.cs3332.data.object.order.OrderStatus;
import com.cs3332.handler.constructor.AbstractBodyHandler;

import java.util.List;
import java.util.stream.Collectors;

public class ListOrdersHandler extends AbstractBodyHandler<ListOrdersPayload> {
    private String token;

    public ListOrdersHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        // Check authorization
        UserInformation requester = dataManager.getAccountInformation(token);
        if (requester == null) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("Invalid token."));
        }

        // Authorize CASHIER, BARTENDER, MANAGER, or ADMIN roles
        List<Role> roles = dataManager.getRole(token);
        if (!roles.contains(Role.CASHIER) && !roles.contains(Role.BARTENDER) 
            && !roles.contains(Role.MANAGER) && !roles.contains(Role.ADMIN)) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, 
                new ErrorResponse("You do not have permission to view orders."));
        }

        List<Order> orders;
        
        try {
            if (payload.getUserID() != null) {
                orders = server.getDataManager().getProductionDBSource().getOrdersByUserID(payload.getUserID().toString());
            } else if (payload.getStatus() != null) {
                orders = server.getDataManager().getProductionDBSource().getOrdersByStatus(payload.getStatus());
            } else {
                orders = server.getDataManager().getProductionDBSource().getAllOrders();
            }

            if (orders == null || orders.isEmpty()) {
                return new ServerResponse(ResponseCode.FOUND, new OrderListResponse(List.of()));
            }

            List<OrderResponse> orderResponses = orders.stream()
                    .map(order -> new OrderResponse(
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
                    ))
                    .collect(Collectors.toList());

            return new ServerResponse(ResponseCode.FOUND, new OrderListResponse(orderResponses));
        } catch (Exception e) {
            return new ServerResponse(ResponseCode.INTERNAL_SERVER_ERROR, 
                new ErrorResponse("Error retrieving orders: " + e.getMessage()));
        }
    }
} 