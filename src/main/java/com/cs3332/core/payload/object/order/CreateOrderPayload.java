package com.cs3332.core.payload.object.order;

import com.cs3332.core.payload.AbstractRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateOrderPayload extends AbstractRequestBody {
    private List<OrderItemPayload> items;
    private final String tableID;
    private final boolean approved;
}