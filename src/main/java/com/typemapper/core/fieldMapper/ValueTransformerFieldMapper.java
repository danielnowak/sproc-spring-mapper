package com.typemapper.core.fieldMapper;

import com.typemapper.core.ValueTransformer;

public class ValueTransformerFieldMapper implements FieldMapper {

    private final ValueTransformer<?, ?> valueTransformer;
    
    public ValueTransformerFieldMapper(final Class<? extends ValueTransformer<?, ?>> valueTransformer)
            throws InstantiationException, IllegalAccessException {
        this.valueTransformer = valueTransformer.newInstance();
    }

    @Override
    public Object mapField(final String string, final Class clazz) {
		if (string == null) {
			return null;
		}
        if (valueTransformer != null) {
            return valueTransformer.transform(string);
        }
        return null;
    }

}
