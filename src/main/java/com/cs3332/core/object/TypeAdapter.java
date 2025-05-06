package com.cs3332.core.object;

public interface TypeAdapter<T> {
    T convert(String value);
}
