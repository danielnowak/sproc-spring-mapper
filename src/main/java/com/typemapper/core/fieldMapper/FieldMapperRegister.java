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
		FieldMapperRegister.register(Integer.class, integerMapper);
		
		FieldMapper intMapper = new IntFieldMapper();
		FieldMapperRegister.register(int.class, intMapper);
		
		FieldMapper longMapper = new LongFieldMapper();
		FieldMapperRegister.register(Long.class, longMapper);
		
		FieldMapper primitiveLongMapper = new PrimitiveLongFieldMapper();
		FieldMapperRegister.register(long.class, primitiveLongMapper);
		
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
		
		FieldMapper enumMapper = new EnumerationFieldMapper();
		FieldMapperRegister.register(Enum.class, enumMapper);
		
	}
	
	@SuppressWarnings("rawtypes")
	private static void register(Class clazz, FieldMapper mapper) {
		register.put(clazz, mapper);
	}
	@SuppressWarnings("rawtypes")
	public static FieldMapper getMapperForClass(Class clazz) {
		if (clazz.getEnumConstants() != null) {
			return register.get(Enum.class);
		}
		return register.get(clazz);
	}

}
