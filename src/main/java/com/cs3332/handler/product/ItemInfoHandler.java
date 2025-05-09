package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.object.*;
import com.cs3332.core.payload.object.product.ItemInfoPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.product.ItemResponse;
import com.cs3332.core.utils.Response;
import com.cs3332.data.object.storage.Item;
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
        if (item != null) {
            return new ServerResponse(ResponseCode.FOUND,
                    new ItemResponse(item.getEntryID(), item.getItemStackID(),
                    item.getImportExportDate(), item.getExpiration_date(), item.getQuantity(),item.getSupplier()));
        } else {
            return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse("Item not found"));
        }
    }
}
