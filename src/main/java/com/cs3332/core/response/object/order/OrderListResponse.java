package com.cs3332.core.response.object.order;

import com.cs3332.core.response.constructor.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponse extends AbstractResponse {
    private List<OrderResponse> orders;
} 