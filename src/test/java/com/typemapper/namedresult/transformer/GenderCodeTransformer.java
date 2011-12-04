package com.typemapper.namedresult.transformer;

import com.typemapper.core.ValueTransformer;
import com.typemapper.namedresult.results.GenderCode;

public class GenderCodeTransformer extends ValueTransformer<String, GenderCode> {

    @Override
    public GenderCode transform(final String string) {
        return GenderCode.fromCode(string);
    }

}
