package com.typemapper.core;

public abstract class ValueAdapter<Value, Bound> {
    
    public abstract Bound unmarshal(String string);
    
}
