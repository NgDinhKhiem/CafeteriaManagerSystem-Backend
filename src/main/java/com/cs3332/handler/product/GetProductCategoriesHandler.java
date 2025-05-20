package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.response.object.product.ProductCategoryResponse;
import com.cs3332.data.object.storage.Product;
import com.cs3332.handler.constructor.AbstractHandler;

import java.util.List;
import java.util.stream.Collectors;

public class GetProductCategoriesHandler extends AbstractHandler {
    public GetProductCategoriesHandler(Server server) {
        super(server, RequestMethod.GET);
    }

    @Override
    protected ServerResponse resolve() {
        List<Product> products = server.getDataManager().getProductionDBSource().getAllProduct();
        List<String> categories = products.stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList());
        
        return new ServerResponse(ResponseCode.FOUND, new ProductCategoryResponse(categories));
    }
} 