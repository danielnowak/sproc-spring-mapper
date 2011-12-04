package com.typemapper.core;

public abstract class ValueTransformer<Value, Bound> {

    public abstract Bound transform(String string);
    
}
