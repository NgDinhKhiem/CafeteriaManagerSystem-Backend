package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.object.*;
import com.cs3332.core.payload.object.product.CreateProductPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.product.ProductResponse;
import com.cs3332.data.object.storage.Ingredient;
import com.cs3332.data.object.storage.Product;
import com.cs3332.handler.constructor.AbstractBodyHandler;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CreateProductHandler extends AbstractBodyHandler<CreateProductPayload> {
    private String token;

    public CreateProductHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        if (!dataManager.getRole(token).contains(Role.ADMIN) && !dataManager.getRole(token).contains(Role.MANAGER)) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("You do not have permission to issue this action!"));
        }
        if (payload.getName() == null || payload.getName().isEmpty()) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Product name is required."));
        }
        if (payload.getUnit() == null || payload.getUnit().isEmpty()) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Unit is required."));
        }
        if (payload.getPrice() <= 0) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Price must be greater than zero."));
        }
        if (payload.getIngredients() == null || payload.getIngredients().isEmpty()) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("At least one ingredient is required."));
        }
        for (Ingredient ingredient : payload.getIngredients()) {
            if (ingredient.getItemStackID() == null) {
                return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Invalid ingredient ItemStack ID."));
            }
            if (ingredient.getQuantity() <= 0) {
                return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Ingredient quantity must be greater than zero."));
            }
            if (server.getDataManager().getProductionDBSource().getItemStack(ingredient.getItemStackID()) == null) {
                return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("ItemStack with ID " + ingredient.getItemStackID() + " does not exist."));
            }
        }
        Product product = new Product(
                UUID.randomUUID(),
                payload.getName(),
                payload.getUnit(),
                payload.getIngredients(),
                payload.getPrice()
        );
        Product created = server.getDataManager().getProductionDBSource().createProduct(product);

        if (created != null) {
            List<Ingredient> ingredientResponses = created.getRecipe().stream()
                    .map(ing -> new Ingredient(ing.getItemStackID(), ing.getQuantity()))
                    .collect(Collectors.toList());
            return new ServerResponse(ResponseCode.CREATED, new ProductResponse(
                    created.getID(),
                    created.getName(),
                    created.getUnit(),
                    created.getPrice(),
                    ingredientResponses
            ));
        } else {
            return new ServerResponse(ResponseCode.INTERNAL_SERVER_ERROR, new ErrorResponse("Failed to create Product"));
        }
    }
}
