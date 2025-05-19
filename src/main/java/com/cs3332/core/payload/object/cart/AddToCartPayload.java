package com.cs3332.core.payload.object.cart;

import com.cs3332.core.payload.AbstractRequestBody;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AddToCartPayload extends AbstractRequestBody {
    private final UUID productId;
    private final int quantity;

    public AddToCartPayload(UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
} 