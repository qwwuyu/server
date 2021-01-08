package com.qwwuyu.lib.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

class Exclusion implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
