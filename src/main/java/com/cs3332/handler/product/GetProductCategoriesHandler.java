package com.cs3332.handler.product;

import com.cs3332.Server;
import com.cs3332.core.response.ServerResponse;
import com.cs3332.handler.BaseHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class GetProductCategoriesHandler extends BaseHandler {
    public GetProductCategoriesHandler(Server server) {
        super(server);
    }

    @Override
    protected ServerResponse handleRequest(HttpExchange exchange) throws IOException {
        // Get all product categories from data manager
        return ServerResponse.success(getDataManager().getProductCategories());
    }
} 