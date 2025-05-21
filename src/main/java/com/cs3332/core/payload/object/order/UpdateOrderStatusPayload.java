package com.cs3332.core.payload.object.order;

import com.cs3332.core.payload.AbstractRequestBody;
import com.cs3332.data.object.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UpdateOrderStatusPayload extends AbstractRequestBody {
    private UUID orderID;
    private String newStatus;
} 