package com.cs3332.handler.constructor;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.payload.AbstractRequestBody;
import com.cs3332.core.utils.Logger;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public abstract class AbstractBodyHandler<V extends AbstractRequestBody> extends AbstractHandler {
    private final Class<V> payloadClass;
    protected V payload;

    @SuppressWarnings("unchecked")
    public AbstractBodyHandler(Server server, RequestMethod method) {
        super(server, method);
        this.payloadClass = (Class<V>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase(this.method.name())) {
            Logger.warn("Wrong HTTP method: " + exchange.getRequestMethod() + ", expected " + this.method.name());
            exchange.sendResponseHeaders(ResponseCode.BAD_REQUEST.getCode(), -1);
            return;
        }

        String body = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));

        if(body.isEmpty()){
            Logger.warn("Body is empty");
            exchange.sendResponseHeaders(ResponseCode.BAD_REQUEST.getCode(), -1);
            return;
        }

        try {
            this.payload = AbstractRequestBody.deserialize(body, payloadClass);
        }catch (Exception e){
            Logger.error(e.getMessage());
        }

        super.handle(exchange);
    }
}
