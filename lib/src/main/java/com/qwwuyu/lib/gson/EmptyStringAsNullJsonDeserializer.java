package com.qwwuyu.lib.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;

/**
 * GSON注解 处理这个动态字段无数据的时候 传递空字符串导致解析出错
 */
class EmptyStringAsNullJsonDeserializer<T> implements JsonDeserializer<T> {
    @Override
    public T deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        if (!GsonHelper.isTypeString(type)) {
            if (jsonElement.isJsonPrimitive()) {
                final JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if (jsonPrimitive.isString() && jsonPrimitive.getAsString().isEmpty()) {
                    return null;
                }
            }
        }
        return context.deserialize(jsonElement, type);
    }
}