package com.cs3332.handler.order;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.Role;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.order.UpdateOrderStatusPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.handler.constructor.AbstractBodyHandler;

public class UpdateOrderStatusHandler extends AbstractBodyHandler<UpdateOrderStatusPayload> {
    private String token;

    public UpdateOrderStatusHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        if (!dataManager.getRole(token).contains(Role.ADMIN) && 
            !dataManager.getRole(token).contains(Role.MANAGER) &&
            !dataManager.getRole(token).contains(Role.BARTENDER)) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, 
                new ErrorResponse("You do not have permission to update order status."));
        }

        if (payload.getOrderId() == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, 
                new ErrorResponse("Order ID is required."));
        }

        if (payload.getStatus() == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, 
                new ErrorResponse("Status is required."));
        }

        boolean success = getDataManager().updateOrderStatus(payload.getOrderId(), payload.getStatus());
        return success ? 
            ServerResponse.success("Order status updated") : 
            ServerResponse.error("Failed to update order status");
    }
} 