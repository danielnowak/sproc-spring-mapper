package com.typemapper.core.fieldMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FieldMapperRegister {
	
	@SuppressWarnings({ "rawtypes"})
	private static final Map<Class, FieldMapper> register = new HashMap<Class, FieldMapper>();
	
	static {
		FieldMapper dateFieldMapper = new DateFieldMapper();
		FieldMapperRegister.register(Date.class, dateFieldMapper);
		
		FieldMapper integerMapper = new IntegerFieldMapper();
		FieldMapperRegister.register(int.class, integerMapper);
		FieldMapperRegister.register(Integer.class, integerMapper);
		
		FieldMapper longMapper = new LongFieldMapper();
		FieldMapperRegister.register(long.class, longMapper);
		FieldMapperRegister.register(Long.class, longMapper);
		
		FieldMapper charMapper = new CharFieldMapper();
		FieldMapperRegister.register(char.class, charMapper);
		FieldMapperRegister.register(Character.class, charMapper);
		
		FieldMapper stringMapper = new StringFieldMapper();
		FieldMapperRegister.register(String.class, stringMapper);
		
		FieldMapper doubleMapper = new DoubleFieldMapper();
		FieldMapperRegister.register(Double.class, doubleMapper);
		FieldMapperRegister.register(double.class, doubleMapper);
		
		FieldMapper floatMapper = new FloatFieldMapper();
		FieldMapperRegister.register(Float.class, floatMapper);
		FieldMapperRegister.register(float.class, floatMapper);
		
		FieldMapper shortMapper = new ShortFieldMapper();
		FieldMapperRegister.register(Short.class, shortMapper);
		FieldMapperRegister.register(short.class, shortMapper);
		
		FieldMapper booleanMapper = new BooleanFieldMapper();
		FieldMapperRegister.register(Boolean.class, booleanMapper);
		FieldMapperRegister.register(boolean.class, booleanMapper);
		
		
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
