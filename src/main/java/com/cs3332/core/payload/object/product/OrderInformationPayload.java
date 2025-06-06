package com.cs3332.core.payload.object.product;

import com.cs3332.core.payload.AbstractRequestBody;
import com.cs3332.data.object.order.OrderItem;
import com.cs3332.data.object.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderInformationPayload extends AbstractRequestBody {
    private String tableID;
    private UUID orderID;
    private List<OrderItem> items;
    private long orderTimestamp;
    private OrderStatus status;
    private String userID; // Username of the cashier/bartender
    private Long paymentTimestamp;
    private Long preparationStartTimestamp; // When bartender starts preparing
    private Long readyTimestamp; // When order is ready for pickup
    private String preparedBy; // Username of the bartender who prepared the order
}
