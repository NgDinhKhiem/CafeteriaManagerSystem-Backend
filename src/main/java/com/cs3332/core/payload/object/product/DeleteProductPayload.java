package com.cs3332.core.payload.object.product;

import com.cs3332.core.payload.AbstractRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteProductPayload extends AbstractRequestBody {
    private UUID productID;
}
