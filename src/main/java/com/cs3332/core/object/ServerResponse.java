package com.cs3332.core.object;


import com.cs3332.core.response.constructor.AbstractResponse;
import lombok.Getter;

@Getter
public class ServerResponse {
    private final ResponseCode code;
    private final AbstractResponse response;

    public ServerResponse(ResponseCode code, AbstractResponse response) {
        this.code = code;
        this.response = response;
    }

}
