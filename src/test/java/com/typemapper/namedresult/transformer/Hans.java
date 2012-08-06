package com.typemapper.namedresult.transformer;

import com.typemapper.core.TypeMapperFactory;

public class Hans {

    static {
        TypeMapperFactory.registerGlobalValueTransformer(Hans.class, new HansValueTransformer());
    }

    private Object value;

    public Hans() { }

    public Hans(final Object value) {
        this.value = value;
    }

    public void setValue(final Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
