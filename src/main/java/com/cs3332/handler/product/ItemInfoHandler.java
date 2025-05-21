package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.product.ItemInfoPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.product.ItemResponse;
import com.cs3332.data.object.storage.Item;
import com.cs3332.data.object.storage.ItemStack;
import com.cs3332.handler.constructor.AbstractBodyHandler;

public class ItemInfoHandler extends AbstractBodyHandler<ItemInfoPayload> {
    public ItemInfoHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        if (payload.getItemID() == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Item ID is required."));
        }
        Item item = server.getDataManager().getProductionDBSource().getItemInfo(payload.getItemID());
        ItemStack itemStack = server.getDataManager().getProductionDBSource().getItemStack(item.getItemStackID());
        if (item != null) {
            return new ServerResponse(ResponseCode.FOUND,
                    new ItemResponse(
                            item.getEntryID(),
                            item.getItemStackID(),
                            itemStack.getName(),
                            itemStack.getUnit(),
                            item.getImportExportDate(),
                            item.getExpiration_date(),
                            item.getQuantity(),
                            item.getSupplier(),
                            item.getReason()));
        } else {
            return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse("Item not found"));
        }
    }
}
