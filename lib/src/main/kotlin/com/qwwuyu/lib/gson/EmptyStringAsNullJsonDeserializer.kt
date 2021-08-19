package com.qwwuyu.lib.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.qwwuyu.lib.gson.GsonHelper
import java.lang.reflect.Type

/**
 * GSON注解 处理这个动态字段无数据的时候 传递空字符串导致解析出错
 */
internal class EmptyStringAsNullJsonDeserializer<T> : JsonDeserializer<T?> {
    @Throws(JsonParseException::class)
    override fun deserialize(jsonElement: JsonElement, type: Type, context: JsonDeserializationContext): T? {
        if (!GsonHelper.isTypeString(type)) {
            if (jsonElement.isJsonPrimitive) {
                val jsonPrimitive = jsonElement.asJsonPrimitive
                if (jsonPrimitive.isString && jsonPrimitive.asString.isEmpty()) {
                    return null
                }
            }
        }
        return context.deserialize(jsonElement, type)
    }
}