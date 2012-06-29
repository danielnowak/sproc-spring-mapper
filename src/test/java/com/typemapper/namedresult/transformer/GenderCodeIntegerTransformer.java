package com.typemapper.namedresult.transformer;

import com.typemapper.core.ValueTransformer;

import com.typemapper.namedresult.results.GenderCode;

public class GenderCodeIntegerTransformer extends ValueTransformer<Integer, GenderCode> {

    @Override
    public GenderCode unmarshalFromDb(final String value) {
        return GenderCode.values()[Integer.valueOf(value)];
    }

    @Override
    public Integer marshalToDb(final GenderCode bound) {
        return bound.ordinal();
    }
}
