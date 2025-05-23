package com.cs3332.core.response.constructor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbstractResponse {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    AbstractResponse.class,
                    new ResponseTypeAdapter())
            .create();

    @SuppressWarnings("unchecked")
    public static <T extends AbstractResponse> T deserialize(String json) {
        return (T) gson.fromJson(json, AbstractResponse.class);
    }

    public String toJSON(){
        return gson.toJson(this);
    }
    @Override
    public String toString() {return toJSON();}
}