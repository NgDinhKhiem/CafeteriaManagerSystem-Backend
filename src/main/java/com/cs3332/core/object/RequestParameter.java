package com.cs3332.core.object;

public class RequestParameter {
    private final String key;
    private final String value;

    public RequestParameter(String key, Object value) {
        this.key = key;
        this.value = value.toString();
    }

    public RequestParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String toURI(){
        return key+"="+value;
    }
}
