package com.cs3332.core.response.constructor;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ResponseTypeAdapter implements JsonSerializer<ResponseTypeAdapter>, JsonDeserializer<ResponseTypeAdapter> {
    @Override
    public JsonElement serialize(ResponseTypeAdapter src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", src.getClass().getName());
        jsonObject.add("properties",context.serialize(src));
        return jsonObject;
    }

    @Override
    public ResponseTypeAdapter deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement properties = jsonObject.get("properties");
        try {
            return context.deserialize(properties, Class.forName(type));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("NULL");
        }
    }
}
