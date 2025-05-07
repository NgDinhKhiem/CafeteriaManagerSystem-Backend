package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.object.*;
import com.cs3332.core.payload.object.product.ProductInfoPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.product.ProductResponse;
import com.cs3332.data.object.storage.Ingredient;
import com.cs3332.data.object.storage.Product;
import com.cs3332.handler.constructor.AbstractBodyHandler;

import java.util.List;
import java.util.stream.Collectors;

public class ProductInfoHandler extends AbstractBodyHandler<ProductInfoPayload> {
    private String token;
    public ProductInfoHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        if (payload.getProductID() == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Product ID is required."));
        }
        Product product = server.getDataManager().getProductionDBSource().getProduct(payload.getProductID());
        if (product != null) {
            List<Ingredient> ingredientResponses = product.getRecipe().stream()
                    .map(ing -> new Ingredient(ing.getItemStackID(), ing.getQuantity()))
                    .collect(Collectors.toList());
            return new ServerResponse(ResponseCode.OK, new ProductResponse(
                    product.getID(),
                    product.getName(),
                    product.getUnit(),
                    product.getPrice(),
                    ingredientResponses
            ));
        } else {
            return new ServerResponse(ResponseCode.NOT_FOUND, new ErrorResponse("Product not found"));
        }
    }
}