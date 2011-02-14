package com.typemapper.core;

public final class AnyAdapter extends ValueAdapter<String, Object> {

    @Override
    public Object unmarshal(String v) {
        return v;
    }

}
