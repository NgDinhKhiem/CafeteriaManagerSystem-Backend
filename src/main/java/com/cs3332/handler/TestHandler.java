package com.cs3332.handler;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.Response;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.response.object.TextResponse;
import com.cs3332.handler.constructor.AbstractBodyHandler;
import com.cs3332.handler.constructor.body.object.TextBody;

public class TestHandler extends AbstractBodyHandler<TextResponse, TextBody> {
    public TestHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected Response<TextResponse> resolve() {
        return new Response<>(ResponseCode.OK, new TextResponse("Hello World + Body:"+this.payload.toJSON()));
    }
}
