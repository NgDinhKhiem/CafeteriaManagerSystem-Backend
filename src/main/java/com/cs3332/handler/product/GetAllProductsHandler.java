package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.object.*;
import com.cs3332.core.response.object.product.ProductListResponse;
import com.cs3332.core.response.object.product.ProductResponse;
import com.cs3332.data.object.storage.Ingredient;
import com.cs3332.data.object.storage.Product;
import com.cs3332.handler.constructor.AbstractHandler;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllProductsHandler extends AbstractHandler {
    public GetAllProductsHandler(Server server) {
        super(server, RequestMethod.GET);
    }

    @Override
    protected ServerResponse resolve() {
        List<Product> products = server.getDataManager().getProductionDBSource().getAllProduct();
        List<ProductResponse> responses = products.stream()
                .map(product -> new ProductResponse(
                        product.getID(),
                        product.getName(),
                        product.getUnit(),
                        product.getPrice(),
                        product.getRecipe().stream()
                                .map(ing -> new Ingredient(ing.getItemStackID(), ing.getQuantity()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        return new ServerResponse(ResponseCode.OK, new ProductListResponse(responses));
    }
}
