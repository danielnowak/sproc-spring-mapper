package com.typemapper.core.fieldMapper;

public abstract class ValueTransformer<Value, Bound> {

    public abstract Bound transform(String string);
    
}
