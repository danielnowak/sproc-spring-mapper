package com.typemapper.adapter;

import com.typemapper.core.ValueAdapter;
import com.typemapper.namedresult.results.GenderEnum;

public class GenderEnumValueExtractor extends ValueAdapter<String, GenderEnum> {

    @Override
    public GenderEnum unmarshal(String v) {
        return GenderEnum.valueOf(v);
    }

}
