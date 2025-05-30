package com.cs3332.core.payload.object.order;

import com.cs3332.core.payload.AbstractRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemPayload extends AbstractRequestBody {
    private UUID productID;
    private int quantity;
} 