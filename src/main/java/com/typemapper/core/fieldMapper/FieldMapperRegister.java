package com.typemapper.core.fieldMapper;

import java.util.HashMap;
import java.util.Map;

public class FieldMapperRegister {
	
	@SuppressWarnings({ "rawtypes"})
	private static final Map<Class, FieldMapper> register = new HashMap<Class, FieldMapper>();
	
	static {
		FieldMapper integerMapper = new IntegerFieldMapper();
		FieldMapperRegister.register(int.class, integerMapper);
		FieldMapperRegister.register(Integer.class, integerMapper);
		
		FieldMapper longMapper = new LongFieldMapper();
		FieldMapperRegister.register(long.class, longMapper);
		FieldMapperRegister.register(Long.class, longMapper);
		
		FieldMapper charMapper = new CharFieldMapper();
		FieldMapperRegister.register(char.class, charMapper);
		
		FieldMapper stringMapper = new StringFieldMapper();
		FieldMapperRegister.register(String.class, stringMapper);
		
	}
	
	@SuppressWarnings("rawtypes")
	static synchronized void register(Class clazz, FieldMapper mapper) {
		register.put(clazz, mapper);
	}
	@SuppressWarnings("rawtypes")
	public static FieldMapper getMapperForClass(Class clazz) {
		return register.get(clazz);
	}

}
