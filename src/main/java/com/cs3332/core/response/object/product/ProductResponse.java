package com.cs3332.core.response.object.product;

import com.cs3332.core.response.constructor.AbstractResponse;
import com.cs3332.data.object.storage.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductResponse extends AbstractResponse {
    private final UUID id;
    private final String name;
    private final String unit;
    private final double price;
    private final List<IngredientResponse> ingredients;
    private final int availableCount; // Number of products that can be made with current inventory
}
