package com.qwwuyu.lib.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

internal class IgnoreTypeAdapter : TypeAdapter<Ignore?>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Ignore?) {
        out.nullValue()
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Ignore {
        `in`.skipValue()
        return Ignore.single
    }
}