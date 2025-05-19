package com.cs3332.handler.order.cart;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.cart.AddToCartPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.cart.CartResponse;
import com.cs3332.handler.constructor.AbstractBodyHandler;

public class AddToCartHandler extends AbstractBodyHandler<AddToCartPayload> {
    private String token;

    public AddToCartHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        if (payload.getProductId() == null) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, 
                new ErrorResponse("Product ID is required."));
        }

        if (payload.getQuantity() <= 0) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, 
                new ErrorResponse("Quantity must be greater than 0."));
        }

        String username = dataManager.getUsernameFromToken(token);
        if (username == null) {
            return new ServerResponse(ResponseCode.UNAUTHORIZED, 
                new ErrorResponse("Invalid token."));
        }

        boolean success = getDataManager().addToCart(
            username,
            payload.getProductId(), 
            payload.getQuantity()
        );

        return new ServerResponse(
            success ? ResponseCode.OK : ResponseCode.INTERNAL_SERVER_ERROR,
            new CartResponse(
                success ? "Item added to cart" : "Failed to add item to cart",
                success
            )
        );
    }
} 