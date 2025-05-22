package com.cs3332.core.payload.object.product;
import com.cs3332.core.payload.AbstractRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IEItemPayload extends AbstractRequestBody {
    private UUID itemStackID;
    private float quantity;
    private long expirationDate;
    private String supplier;
    private String reason;
}
