package com.typemapper.core.fieldMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FieldMapperRegister {
	
	@SuppressWarnings({ "rawtypes"})
	private static final Map<Class, FieldMapper> register = new HashMap<Class, FieldMapper>();
	
	static {
		final FieldMapper dateFieldMapper = new DateFieldMapper();
		FieldMapperRegister.register(Date.class, dateFieldMapper);
		
		final FieldMapper integerMapper = new IntegerFieldMapper();
		FieldMapperRegister.register(Integer.class, integerMapper);
		
		final FieldMapper intMapper = new IntFieldMapper();
		FieldMapperRegister.register(int.class, intMapper);
		
		final FieldMapper longMapper = new LongFieldMapper();
		FieldMapperRegister.register(Long.class, longMapper);
		
		final FieldMapper primitiveLongMapper = new PrimitiveLongFieldMapper();
		FieldMapperRegister.register(long.class, primitiveLongMapper);
		
		final FieldMapper charMapper = new CharFieldMapper();
		FieldMapperRegister.register(char.class, charMapper);
		FieldMapperRegister.register(Character.class, charMapper);
		
		final FieldMapper stringMapper = new StringFieldMapper();
		FieldMapperRegister.register(String.class, stringMapper);
		
		final FieldMapper doubleMapper = new DoubleFieldMapper();
		FieldMapperRegister.register(Double.class, doubleMapper);
		FieldMapperRegister.register(double.class, doubleMapper);
		
		final FieldMapper floatMapper = new FloatFieldMapper();
		FieldMapperRegister.register(Float.class, floatMapper);
		FieldMapperRegister.register(float.class, floatMapper);
		
		final FieldMapper shortMapper = new ShortFieldMapper();
		FieldMapperRegister.register(Short.class, shortMapper);
		FieldMapperRegister.register(short.class, shortMapper);
		
		final FieldMapper booleanMapper = new BooleanFieldMapper();
		FieldMapperRegister.register(Boolean.class, booleanMapper);
		FieldMapperRegister.register(boolean.class, booleanMapper);
		
		final FieldMapper enumMapper = new EnumrationFieldMapper();
		FieldMapperRegister.register(Enum.class, enumMapper);

		final FieldMapper bigDecimalMapper = new BigDecimalFieldMappper();
		FieldMapperRegister.register(BigDecimal.class, bigDecimalMapper);
                
                final FieldMapper hstoreMapper = new HStoreFieldMapper();
                FieldMapperRegister.register(Map.class, hstoreMapper);
	}
	
	@SuppressWarnings("rawtypes")
	private static void register(final Class clazz, final FieldMapper mapper) {
		register.put(clazz, mapper);
	}
	@SuppressWarnings("rawtypes")
	public static FieldMapper getMapperForClass(final Class clazz) {
		if (clazz.getEnumConstants() != null) {
			return register.get(Enum.class);
		}
		return register.get(clazz);
	}

}
