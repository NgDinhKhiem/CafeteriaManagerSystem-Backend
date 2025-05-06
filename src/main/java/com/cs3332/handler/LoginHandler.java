package com.cs3332.handler;

import com.cs3332.Server;
import com.cs3332.core.object.Param;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.payload.object.LoginPayload;
import com.cs3332.core.response.object.TextResponse;
import com.cs3332.handler.constructor.AbstractBodyHandler;

public class LoginHandler extends AbstractBodyHandler<LoginPayload> {
    @Param
    private String role;
    public LoginHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        return new ServerResponse(ResponseCode.OK, new TextResponse("Login Successful"));
    }
}
