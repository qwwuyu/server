package com.qwwuyu.lib.gson

import com.google.gson.TypeAdapter
import com.google.gson.internal.bind.util.ISO8601Utils
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.text.DateFormat
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

internal class DateTypeAdapter : TypeAdapter<Date?>() {
    private val dateFormats: MutableList<DateFormat> = ArrayList()

    init {
        dateFormats.add(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()))
        dateFormats.add(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT))
        if (Locale.getDefault() != Locale.US) {
            dateFormats.add(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US))
        }
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Date? {
        val peek = `in`.peek()
        if (peek == JsonToken.NULL) {
            `in`.nextNull()
            return null
        } else if (peek == JsonToken.NUMBER) {
            return Date(`in`.nextLong())
        }
        return deserializeToDate(`in`.nextString())
    }

    @Synchronized
    private fun deserializeToDate(json: String?): Date? {
        if (json == null || json.isEmpty()) {
            return null
        }
        try {
            if (json.matches("^[\\d]{13}$".toRegex())) {
                return Date(json.toLong())
            }
        } catch (ignored: Exception) {
        }
        for (dateFormat in dateFormats) {
            try {
                return dateFormat.parse(json)
            } catch (ignored: Exception) {
            }
        }
        try {
            return ISO8601Utils.parse(json, ParsePosition(0))
        } catch (e: Exception) {
            //throw new JsonSyntaxException(json, e);
        }
        return null
    }

    @Synchronized
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Date?) {
        if (value == null) {
            out.nullValue()
            return
        }
        val dateFormatAsString = dateFormats[0].format(value)
        out.value(dateFormatAsString)
    }
}