package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.Role;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.product.IEItemPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.TextResponse;
import com.cs3332.core.utils.Response;
import com.cs3332.core.utils.Utils;
import com.cs3332.data.object.storage.Item;
import com.cs3332.data.object.storage.ItemStack;
import com.cs3332.handler.constructor.AbstractBodyHandler;

import java.util.UUID;

public class ExportItemHandler extends AbstractBodyHandler<IEItemPayload> {
    private String token;

    public ExportItemHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        if (!dataManager.getRole(token).contains(Role.ADMIN) && !dataManager.getRole(token).contains(Role.MANAGER)) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("You do not have permission to issue this action!"));
        }
        if (payload.getItemStackID() == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("ItemStack ID is required."));
        }
        if (payload.getQuantity() <= 0) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Quantity must be greater than zero."));
        }
        ItemStack itemStack = server.getDataManager().getProductionDBSource().getItemStack(payload.getItemStackID());
        if (itemStack == null) {
            return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse("ItemStack not found"));
        }
        Item item = new Item(
                itemStack.getID(),
                UUID.randomUUID(),
                Utils.getTime(),
                -1,
                -payload.getQuantity(),
                payload.getSupplier(),
                payload.getReason()
        );
        Response response = server.getDataManager().getProductionDBSource().exportItem(item);
        if (response.getState()) {
            return new ServerResponse(ResponseCode.OK, new TextResponse("Item exported successfully"));
        } else {
            return new ServerResponse(ResponseCode.INTERNAL_SERVER_ERROR, new ErrorResponse(response.getResponse()));
        }
    }
}