package com.cs3332.core.payload;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbstractRequestBody{
    public static final Gson gson = new GsonBuilder().create();

    @SuppressWarnings("unchecked")
    public static <T extends AbstractRequestBody> T deserialize(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public String toJSON(){
        return gson.toJson(this);
    }
}