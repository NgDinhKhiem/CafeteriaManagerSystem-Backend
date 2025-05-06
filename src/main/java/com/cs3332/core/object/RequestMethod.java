package com.cs3332.core.object;

public enum RequestMethod {
    GET,
    POST,
    PUT,
    DELETE,
    HEAD,
    OPTIONS,
    TRACE,
    CONNECT,
    PATCH;

    public static RequestMethod fromString(String method) {
        if (method == null) return null;
        try {
            return RequestMethod.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Optionally log or throw a custom exception
        }
    }
}

