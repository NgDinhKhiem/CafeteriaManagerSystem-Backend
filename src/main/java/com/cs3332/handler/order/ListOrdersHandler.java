package com.cs3332.handler.order;

import com.cs3332.Server;
import com.cs3332.core.object.OptionalParam;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.order.OrderListResponse;
import com.cs3332.core.response.object.order.OrderResponse;
import com.cs3332.data.object.order.Order;
import com.cs3332.data.object.order.OrderStatus;
import com.cs3332.handler.constructor.AbstractHandler;

import java.util.List;
import java.util.stream.Collectors;

public class ListOrdersHandler extends AbstractHandler {
//    private String token;
    @OptionalParam
    private String tableID;
    @OptionalParam
    private OrderStatus orderStatus;
    @OptionalParam
    private long from;
    @OptionalParam
    private long to;

    public ListOrdersHandler(Server server) {
        super(server, RequestMethod.GET);
    }

    @Override
    protected ServerResponse resolve() {
        // Check authorization
//        UserInformation requester = dataManager.getAccountInformation(token);
//        if (requester == null) {
//            return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("Invalid token."));
//        }

        // Authorize CASHIER, BARTENDER, MANAGER, or ADMIN roles
        /*List<Role> roles = dataManager.getRole(token);
        if (!roles.contains(Role.CASHIER) && !roles.contains(Role.BARTENDER) 
            && !roles.contains(Role.MANAGER) && !roles.contains(Role.ADMIN)) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, 
                new ErrorResponse("You do not have permission to view orders."));
        }*/

        try {
            List<Order> orders = server.getDataManager().getProductionDBSource().getOrders(tableID, orderStatus, from, to);

            if (orders == null) {
                return new ServerResponse(ResponseCode.NOT_FOUND, new OrderListResponse(List.of()));
            }

            if (orders.isEmpty()) {
                return new ServerResponse(ResponseCode.FOUND, new OrderListResponse(List.of()));
            }

            List<OrderResponse> orderResponses = orders.stream()
                    .map(order -> new OrderResponse(
                            order.getTableID(),
                            order.getOrderID(),
                            order.getItems(),
                            order.getOrderTimestamp(),
                            order.getStatus(),
                            order.getUserID(),
                            order.getPaymentTimestamp(),
                            order.getPreparationStartTimestamp(),
                            order.getReadyTimestamp(),
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