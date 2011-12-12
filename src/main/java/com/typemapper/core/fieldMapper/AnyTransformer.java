package com.typemapper.core.fieldMapper;

import com.typemapper.core.ValueTransformer;

public class AnyTransformer extends ValueTransformer<String, Object> {

    @Override
    public Object transform(final String v) {
        return v;
    }

}
