package com.qwwuyu.lib.gson;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

class FloatTypeAdapter extends TypeAdapter<Number> {
    private final Float defaultValue;

    public FloatTypeAdapter(Float defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public void write(JsonWriter out, Number value) throws IOException {
        out.value(value);
    }

    @Override
    public Number read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        try {
            String result = in.nextString();
            if ("".equals(result)) {
                return defaultValue;
            }
            return Float.parseFloat(result);
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }
}