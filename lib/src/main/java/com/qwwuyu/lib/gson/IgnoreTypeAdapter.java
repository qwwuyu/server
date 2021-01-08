package com.qwwuyu.lib.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

class IgnoreTypeAdapter extends TypeAdapter<Ignore> {
    @Override
    public void write(final JsonWriter out, final Ignore value) throws IOException {
        out.nullValue();
    }

    @Override
    public Ignore read(final JsonReader in) throws IOException {
        in.skipValue();
        return Ignore.single;
    }
}