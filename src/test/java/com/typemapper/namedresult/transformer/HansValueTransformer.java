package com.typemapper.namedresult.transformer;

import com.typemapper.core.ValueTransformer;

public class HansValueTransformer extends ValueTransformer<String, Hans> {

    @Override
    public Hans unmarshalFromDb(final String value) {
        return new Hans(value);
    }

    @Override
    public String marshalToDb(final Hans bound) {
        return String.valueOf(bound.getValue());
    }
}
