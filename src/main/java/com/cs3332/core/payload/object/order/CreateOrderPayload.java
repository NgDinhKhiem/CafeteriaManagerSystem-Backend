package com.cs3332.core.payload.object.order;

import com.cs3332.core.payload.AbstractRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderPayload extends AbstractRequestBody {
    private List<OrderItemPayload> items;
    // private String customerId; // Optional: if you want to associate orders with specific customers
} 