package com.typemapper.core;

public class TypeMapperFactory {
	
	public static final <ITEM> TypeMapper<ITEM> createTypeMapper(Class<ITEM> clazz) {
		return new TypeMapper<ITEM>(clazz);
	}

}
