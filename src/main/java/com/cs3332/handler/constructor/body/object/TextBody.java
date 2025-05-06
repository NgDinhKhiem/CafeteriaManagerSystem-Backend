package com.cs3332.handler.constructor.body.object;

import com.cs3332.handler.constructor.body.AbstractRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TextBody extends AbstractRequestBody {
    private final String text;
}
