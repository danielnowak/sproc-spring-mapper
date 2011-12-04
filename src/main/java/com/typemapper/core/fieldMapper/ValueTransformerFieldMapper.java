package com.typemapper.core.fieldMapper;

import com.typemapper.core.ValueTransformer;

public class ValueTransformerFieldMapper implements FieldMapper {

    private final ValueTransformer<String, ?> valueTransformer;
    
    public ValueTransformerFieldMapper(final Class<? extends ValueTransformer<String, ?>> valueTransformer)
            throws InstantiationException, IllegalAccessException {
        this.valueTransformer = valueTransformer.newInstance();
    }

    @Override
    public Object mapField(final String string, final Class clazz) {
        if (valueTransformer != null) {
            return valueTransformer.transform(string);
        }
        return null;
    }

}
