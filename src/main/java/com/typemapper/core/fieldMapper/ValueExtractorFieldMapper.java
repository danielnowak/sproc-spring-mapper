package com.typemapper.core.fieldMapper;

import com.typemapper.core.ValueAdapter;

public class ValueExtractorFieldMapper implements FieldMapper {

    private ValueAdapter<?, ?> valueAdapter;
    
    public ValueExtractorFieldMapper(Class<? extends ValueAdapter<?, ?>> valueAdapter) throws InstantiationException, IllegalAccessException {
        this.valueAdapter = valueAdapter.newInstance();
    }
    
    @Override
    public Object mapField(String string) {
        if (valueAdapter != null) {
            return valueAdapter.unmarshal(string);
        }
        return null;
    }

}
