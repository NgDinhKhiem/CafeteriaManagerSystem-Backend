package com.cs3332.core.response.object.product;

import com.cs3332.core.response.constructor.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;


@Getter
@AllArgsConstructor
public class IngredientResponse extends AbstractResponse {
    private final UUID itemStackID;
    private final String name;
    private final String unit;
    private final float quantity;
}
