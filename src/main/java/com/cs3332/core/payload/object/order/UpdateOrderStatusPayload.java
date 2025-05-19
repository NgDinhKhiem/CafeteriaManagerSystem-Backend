package com.cs3332.core.payload.object.order;

import com.cs3332.core.payload.AbstractRequestBody;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateOrderStatusPayload extends AbstractRequestBody {
    private final UUID orderId;
    private final String status;

    public UpdateOrderStatusPayload(UUID orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }
} 