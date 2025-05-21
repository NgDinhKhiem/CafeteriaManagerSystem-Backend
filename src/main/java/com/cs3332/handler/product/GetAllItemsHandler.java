package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.response.object.product.ItemListResponse;
import com.cs3332.core.response.object.product.ItemResponse;
import com.cs3332.data.object.storage.Item;
import com.cs3332.data.object.storage.ItemStack;
import com.cs3332.handler.constructor.AbstractHandler;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllItemsHandler extends AbstractHandler {
    public GetAllItemsHandler(Server server) {
        super(server, RequestMethod.GET);
    }

    @Override
    protected ServerResponse resolve() {
        List<Item> items = server.getDataManager().getProductionDBSource().getAllItem();
        List<ItemResponse> responses = items.stream()
                .map(i -> {
                    ItemStack itemStack = server.getDataManager().getProductionDBSource().getItemStack(i.getItemStackID());
                    return new ItemResponse(
                            i.getEntryID(),
                            i.getItemStackID(),
                            itemStack.getName(),
                            itemStack.getUnit(),
                            i.getImportExportDate(),
                            i.getExpiration_date(),
                            i.getQuantity(),
                            i.getSupplier());
                })
                .collect(Collectors.toList());
        return new ServerResponse(ResponseCode.FOUND, new ItemListResponse(responses));
    }
}