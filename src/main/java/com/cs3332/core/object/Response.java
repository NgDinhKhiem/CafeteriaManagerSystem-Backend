package com.cs3332.core.object;


import com.cs3332.core.response.constructor.AbstractResponse;
import lombok.Getter;

@Getter
public class Response <T extends AbstractResponse>{
    private final ResponseCode code;
    private final T response;

    public Response(ResponseCode code, T response) {
        this.code = code;
        this.response = response;
    }

}
