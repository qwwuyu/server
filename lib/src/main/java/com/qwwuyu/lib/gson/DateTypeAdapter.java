package com.qwwuyu.lib.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DateTypeAdapter extends TypeAdapter<Date> {
    private final List<DateFormat> dateFormats = new ArrayList<>();

    public DateTypeAdapter() {
        dateFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()));
        dateFormats.add(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
        if (!Locale.getDefault().equals(Locale.US)) {
            dateFormats.add(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US));
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        if (peek == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else if (peek == JsonToken.NUMBER) {
            return new Date(in.nextLong());
        }
        return deserializeToDate(in.nextString());
    }

    private synchronized Date deserializeToDate(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            if (json.matches("^[\\d]{13}$")) {
                return new Date(Long.parseLong(json));
            }
        } catch (Exception ignored) {
        }
        for (DateFormat dateFormat : dateFormats) {
            try {
                return dateFormat.parse(json);
            } catch (Exception ignored) {
            }
        }
        try {
            return ISO8601Utils.parse(json, new ParsePosition(0));
        } catch (Exception e) {
            //throw new JsonSyntaxException(json, e);
        }
        return null;
    }

    @Override
    public synchronized void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        String dateFormatAsString = dateFormats.get(0).format(value);
        out.value(dateFormatAsString);
    }
}