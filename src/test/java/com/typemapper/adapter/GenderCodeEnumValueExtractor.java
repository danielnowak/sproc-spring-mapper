package com.typemapper.adapter;

import com.typemapper.core.ValueAdapter;
import com.typemapper.namedresult.results.GenderCodeEnum;

public class GenderCodeEnumValueExtractor extends ValueAdapter<String, GenderCodeEnum> {

    @Override
    public GenderCodeEnum unmarshal(String string) {
        return GenderCodeEnum.getCode(string);
    }


}
