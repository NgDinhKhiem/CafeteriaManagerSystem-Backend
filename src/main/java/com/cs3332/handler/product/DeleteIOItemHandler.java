package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.object.*;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.TextResponse;
import com.cs3332.core.utils.Response;
import com.cs3332.handler.constructor.AbstractHandler;

import java.util.UUID;

public class DeleteIOItemHandler extends AbstractHandler {
    private String token;

    @Param
    private UUID entryID;

    public DeleteIOItemHandler(Server server) {
        super(server, RequestMethod.DELETE);
    }

    @Override
    protected ServerResponse resolve() {
        if (!dataManager.getRole(token).contains(Role.ADMIN) && !dataManager.getRole(token).contains(Role.MANAGER)) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("You do not have permission to issue this action!"));
        }
        if (entryID == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Entry ID is required."));
        }
        Response response = server.getDataManager().getProductionDBSource().deleteIOItem(entryID);
        if (response.getState()) {
            return new ServerResponse(ResponseCode.OK, new TextResponse("Item entry deleted successfully"));
        } else {
            return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse(response.getResponse()));
        }
    }
}