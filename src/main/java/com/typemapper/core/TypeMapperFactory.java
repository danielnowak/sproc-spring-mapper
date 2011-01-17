package com.typemapper.core;

public class TypeMapperFactory {
	
	@SuppressWarnings("rawtypes")
	public static final TypeMapper createTypeMapper(Class clazz) {
		return new TypeMapper(clazz);
	}

}
