package com.cs3332.core.response.object.order;

import com.cs3332.core.response.AbstractResponse;
import com.cs3332.data.object.order.Order;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderListResponse extends AbstractResponse {
    private final List<Order> orders;

    public OrderListResponse(List<Order> orders) {
        this.orders = orders;
    }
} 