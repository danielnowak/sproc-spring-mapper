package com.typemapper.namedresult.transformer;

import com.typemapper.core.ValueTransformer;

import com.typemapper.namedresult.results.GenderCode;

public class GenderCodeTransformer extends ValueTransformer<String, GenderCode> {

    @Override
    public GenderCode unmarshalFromDb(final String value) {
        return GenderCode.fromCode(value);
    }

    @Override
    public String marshalToDb(final GenderCode bound) {
        return bound.getCode();
    }

}
