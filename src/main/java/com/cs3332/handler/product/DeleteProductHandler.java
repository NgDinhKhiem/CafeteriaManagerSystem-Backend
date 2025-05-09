package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.object.*;
import com.cs3332.core.payload.object.product.DeleteProductPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.TextResponse;
import com.cs3332.handler.constructor.AbstractBodyHandler;

public class DeleteProductHandler extends AbstractBodyHandler<DeleteProductPayload> {
    private String token;

    public DeleteProductHandler(Server server) {
        super(server, RequestMethod.DELETE);
    }

    @Override
    protected ServerResponse resolve() {
        if (!dataManager.getRole(token).contains(Role.ADMIN) && !dataManager.getRole(token).contains(Role.MANAGER)) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("You do not have permission to issue this action!"));
        }
        if (payload.getProductID() == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Product ID is required."));
        }
        String errorMsg = server.getDataManager().getProductionDBSource().removeProduct(payload.getProductID());
        if (errorMsg == null) {
            return new ServerResponse(ResponseCode.OK, new TextResponse("Product deleted successfully"));
        } else {
            return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse(errorMsg));
        }
    }
}
