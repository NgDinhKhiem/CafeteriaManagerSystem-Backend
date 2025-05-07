package com.cs3332.core.response.object.product;

import com.cs3332.core.response.constructor.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class ItemStackListResponse extends AbstractResponse {
    private final List<ItemStackResponse> itemStacks;
}
