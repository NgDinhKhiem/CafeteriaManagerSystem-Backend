package com.cs3332.core.utils;

@FunctionalInterface
public interface ResponseHandler {
    void execute(Response res);
}
