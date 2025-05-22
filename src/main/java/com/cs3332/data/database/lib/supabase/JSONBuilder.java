package com.cs3332.data.database.lib.supabase;

import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;

@NoArgsConstructor
public class JSONBuilder {
    private final JSONObject jsonObject = new JSONObject();

    @SuppressWarnings("unchecked")
    public JSONBuilder pl(String key, Object value){
        jsonObject.put(key, value.toString());
        return this;
    }

    public JSONBuilder(Object object){
        if (object == null) return;

        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                pl(field.getName(), value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }
    }

    public static JSONObject toJSONObj(Object object){
        return new JSONBuilder(object).build();
    }

    public JSONObject build(){
        return this.jsonObject;
    }
}
