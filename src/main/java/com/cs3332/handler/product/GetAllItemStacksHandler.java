package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.object.*;
import com.cs3332.core.response.object.product.ItemStackListResponse;
import com.cs3332.core.response.object.product.ItemStackResponse;
import com.cs3332.data.object.storage.ItemStack;
import com.cs3332.handler.constructor.AbstractHandler;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllItemStacksHandler extends AbstractHandler {
    public GetAllItemStacksHandler(Server server) {
        super(server, RequestMethod.GET);
    }

    @Override
    protected ServerResponse resolve() {
        List<ItemStack> itemStacks = server.getDataManager().getProductionDBSource().getAllItemStack();
        List<ItemStackResponse> responses = itemStacks.stream()
                .map(i -> new ItemStackResponse(i.getID(), i.getName(), i.getUnit()))
                .collect(Collectors.toList());
        return new ServerResponse(ResponseCode.FOUND, new ItemStackListResponse(responses));
    }
}
