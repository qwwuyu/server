package com.qwwuyu.lib.gson

import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.lang.NumberFormatException

internal class FloatTypeAdapter(private val defaultValue: Float) : TypeAdapter<Number?>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Number?) {
        out.value(value)
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Number? {
        if (`in`.peek() == JsonToken.NULL) {
            `in`.nextNull()
            return null
        }
        return try {
            val result = `in`.nextString()
            if ("" == result) {
                defaultValue
            } else result.toFloat()
        } catch (e: NumberFormatException) {
            throw JsonSyntaxException(e)
        }
    }
}