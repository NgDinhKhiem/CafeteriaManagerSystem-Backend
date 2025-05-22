package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.Role;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.product.CreateItemStackPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.product.ItemStackResponse;
import com.cs3332.data.object.storage.Item;
import com.cs3332.data.object.storage.ItemStack;
import com.cs3332.handler.constructor.AbstractBodyHandler;

import java.util.List;
import java.util.UUID;

public class CreateItemStackHandler extends AbstractBodyHandler<CreateItemStackPayload> {
    private String token;

    public CreateItemStackHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        if (!dataManager.getRole(token).contains(Role.ADMIN)
                && !dataManager.getRole(token).contains(Role.MANAGER)
                && !dataManager.getRole(token).contains(Role.STORAGE_MANAGER)
        ) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("You do not have permission to issue this action!"));
        }
        if (payload.getName() == null || payload.getName().isEmpty()) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Item name is required."));
        }
        if (payload.getUnit() == null || payload.getUnit().isEmpty()) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Unit is required."));
        }
        ItemStack itemStack = new ItemStack(UUID.randomUUID(), payload.getName(), payload.getUnit());
        ItemStack created = server.getDataManager().getProductionDBSource().createItemStack(itemStack);

        if (created != null) {
            List<Item> items = server.getDataManager().getProductionDBSource().getItemByID(created.getID());
            double quantity = 0;
            for (Item item : items) {
                quantity += item.getQuantity();
            }
            return new ServerResponse(ResponseCode.CREATED, new ItemStackResponse(
                    created.getID(), created.getName(), created.getUnit(), quantity
            ));
        } else {
            return new ServerResponse(ResponseCode.INTERNAL_SERVER_ERROR, new ErrorResponse("Failed to create ItemStack"));
        }
    }
}