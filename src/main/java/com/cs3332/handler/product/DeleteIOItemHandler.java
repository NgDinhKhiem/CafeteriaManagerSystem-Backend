package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.Role;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.product.DeleteIOItemPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.TextResponse;
import com.cs3332.core.utils.Response;
import com.cs3332.handler.constructor.AbstractBodyHandler;

public class DeleteIOItemHandler extends AbstractBodyHandler<DeleteIOItemPayload> {
    private String token;

    public DeleteIOItemHandler(Server server) {
        super(server, RequestMethod.DELETE);
    }

    @Override
    protected ServerResponse resolve() {
        if (!dataManager.getRole(token).contains(Role.ADMIN) && !dataManager.getRole(token).contains(Role.MANAGER)) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("You do not have permission to issue this action!"));
        }
        if (payload.getEntryID() == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Entry ID is required."));
        }
        Response response = server.getDataManager().getProductionDBSource().deleteIOItem(payload.getEntryID());
        if (response.getState()) {
            return new ServerResponse(ResponseCode.OK, new TextResponse("Item entry deleted successfully"));
        } else {
            return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse(response.getResponse()));
        }
    }
}