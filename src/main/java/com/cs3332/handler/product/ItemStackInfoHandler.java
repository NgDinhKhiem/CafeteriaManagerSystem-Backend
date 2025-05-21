package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.product.ItemStackInfoPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.product.ItemStackResponse;
import com.cs3332.data.object.storage.Item;
import com.cs3332.data.object.storage.ItemStack;
import com.cs3332.handler.constructor.AbstractBodyHandler;

import java.util.List;

public class ItemStackInfoHandler extends AbstractBodyHandler<ItemStackInfoPayload> {
    private String token;

    public ItemStackInfoHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        if (payload.getItemStackID() == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("ItemStack ID is required."));
        }
        ItemStack itemStack = server.getDataManager().getProductionDBSource().getItemStack(payload.getItemStackID());
        if (itemStack != null) {
            List<Item> items = server.getDataManager().getProductionDBSource().getItemByID(itemStack.getID());
            double quantity = 0;
            for (Item item : items) {
                quantity += item.getQuantity();
            }
            return new ServerResponse(ResponseCode.FOUND, new ItemStackResponse(itemStack.getID(), itemStack.getName(), itemStack.getUnit(), quantity));
        } else {
            return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse("ItemStack not found"));
        }
    }
}