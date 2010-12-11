package com.typemapper.core;

public class TypeMapperFactory {
	
	public static final TypeMapper createTypeMapper(Class clazz) {
		return new TypeMapper(clazz);
	}

}
