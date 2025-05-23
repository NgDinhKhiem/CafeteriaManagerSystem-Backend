package com.cs3332.data.object.order;

import com.cs3332.core.response.constructor.AbstractResponse;
import com.cs3332.core.utils.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends AbstractResponse {
    private UUID productID;
    private String productName;
    private String unit;
    private int quantity;
    private double priceAtOrder;
} 