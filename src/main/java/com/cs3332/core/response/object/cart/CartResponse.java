package com.cs3332.core.response.object.cart;

import com.cs3332.core.response.constructor.AbstractResponse;
import com.cs3332.data.object.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse extends AbstractResponse {
    private List<OrderItem> items;
    private double totalAmount;
    private String message;
    private boolean success;

    public CartResponse(List<OrderItem> items, double totalAmount) {
        this.items = items;
        this.totalAmount = totalAmount;
        this.success = true;
        this.message = "Cart retrieved successfully";
    }

    public CartResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
        this.items = List.of();
        this.totalAmount = 0.0;
    }
} 