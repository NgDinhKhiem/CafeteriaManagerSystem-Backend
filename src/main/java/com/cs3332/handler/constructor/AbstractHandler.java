package com.cs3332.handler.constructor;

import com.cs3332.Server;
import com.cs3332.core.object.*;
import com.cs3332.core.response.constructor.AbstractResponse;
import com.cs3332.core.utils.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractHandler<T extends AbstractResponse> implements HttpHandler {

    protected final Server server;
    protected final RequestMethod method;

    public AbstractHandler(Server server, RequestMethod method) {
        this.server = server;
        this.method = method;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase(this.method.name())) {
            Logger.warn("Wrong HTTP method: {}, expected {}", exchange.getRequestMethod(), this.method.name());
            exchange.sendResponseHeaders(ResponseCode.METHOD_NOT_ALLOWED.getCode(), -1);
            return;
        }

        Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
        List<Field> paramFields = new ArrayList<>(List.of(this.getClass().getDeclaredFields()));
        paramFields.removeIf(field -> !field.isAnnotationPresent(Param.class));

        Logger.info("Handling request for URI: {} with handler {}", exchange.getRequestURI(), this.getClass().getSimpleName());

        if (queryParams.size() != paramFields.size()) {
            Logger.warn("Query param size mismatch. Provided: {}, Expected: {}", queryParams.size(), paramFields.size());
            Logger.debug("Provided Params: {}", String.join(", ", queryParams.keySet()));
            Logger.debug("Expected Fields: {}", paramFields.stream().map(Field::getName).collect(Collectors.joining(", ")));

            exchange.sendResponseHeaders(ResponseCode.UNPROCESSABLE_ENTITY.getCode(), -1);
            return;
        }

        for (Field field : paramFields) {
            String fieldName = field.getName();
            if (!queryParams.containsKey(fieldName)) {
                Logger.warn("Missing required query param: {}", fieldName);
                exchange.sendResponseHeaders(ResponseCode.BAD_REQUEST.getCode(), -1);
                return;
            }

            field.setAccessible(true);
            try {
                Object value = TypeHandler.getAdapter(field.getType()).convert(queryParams.get(fieldName));
                field.set(this, value);
            } catch (IllegalAccessException e) {
                Logger.error("Failed to set field value via reflection: {}", e.getMessage());
                exchange.sendResponseHeaders(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), -1);
                return;
            } finally {
                field.setAccessible(false);
            }
        }

        Response<T> response = resolve();
        String responseBody = response.getResponse().toJSON();
        byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(response.getCode().getCode(), responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null && !query.isBlank()) {
            for (String pair : query.split("&")) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }

    protected abstract Response<T> resolve();
}
