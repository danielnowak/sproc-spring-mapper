package com.typemapper.core.fieldMapper;

public class AnyTransformer extends ValueTransformer<String, Object> {

    @Override
    public Object transform(final String v) {
        return v;
    }

}
