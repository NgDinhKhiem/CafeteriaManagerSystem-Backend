package com.cs3332.core.payload.object.product;

import com.cs3332.core.payload.AbstractRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateItemStackPayload extends AbstractRequestBody {
    private String name;
    private String unit;
}
