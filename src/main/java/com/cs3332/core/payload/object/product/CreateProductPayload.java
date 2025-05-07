package com.cs3332.core.payload.object.product;

import com.cs3332.core.payload.AbstractRequestBody;
import com.cs3332.data.object.storage.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductPayload extends AbstractRequestBody {
    private String name;
    private String unit;
    private float price;
    private List<Ingredient> ingredients;
}
