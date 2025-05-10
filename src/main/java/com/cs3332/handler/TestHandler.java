package com.cs3332.handler;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.TextBody;
import com.cs3332.core.response.object.TextResponse;
import com.cs3332.handler.constructor.AbstractBodyHandler;

public class TestHandler extends AbstractBodyHandler<TextBody> {
    public TestHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        return new ServerResponse(ResponseCode.OK, new TextResponse("Hello World + Body:"+this.payload.toJSON()));
    }
}
