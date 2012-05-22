package com.typemapper.core.fieldMapper;

import com.typemapper.core.ValueTransformer;

public class AnyTransformer extends ValueTransformer<String, Object> {

    @Override
    public Object unmarshalFromDb(final String v) {
        return v;
    }

    @Override
    public String marshalToDb(final Object bound) {
        return String.valueOf(bound);
    }
}
