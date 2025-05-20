package com.cs3332.core.response.object.product;

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
public class ProductCategoryResponse extends AbstractResponse {
    private List<String> categories;
} 