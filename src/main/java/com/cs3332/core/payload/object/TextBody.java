package com.cs3332.core.payload.object;

import com.cs3332.core.payload.AbstractRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TextBody extends AbstractRequestBody {
    private final String text;
}
