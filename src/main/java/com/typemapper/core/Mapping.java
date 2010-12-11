package com.typemapper.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.typemapper.annotations.DatabaseField;
import com.typemapper.core.fieldMapper.FieldMapper;
import com.typemapper.core.fieldMapper.FieldMapperRegister;
import com.typemapper.exception.NotsupportedTypeException;


public class Mapping {
	
	@SuppressWarnings("unchecked")
	private final String name;
	private final Field field;
	
	@SuppressWarnings("unchecked")
	static List<Mapping> getMappingsForClass(Class clazz) {
		List<Mapping> result = new ArrayList<Mapping>();
		Field[] fields = clazz.getDeclaredFields();
		for (final Field field : fields) {
			DatabaseField annotation = field.getAnnotation(DatabaseField.class);
			if (annotation != null) {
				result.add(new Mapping(field, annotation.name()));
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	Mapping(Field field, String name) {
		this.name = name;
		this.field = field;
	}
	
	@SuppressWarnings("unchecked")
	public Class getFieldClass() {
		return field.getType();
	}
	
	@SuppressWarnings("unchecked")
	public Method getSetter() {
		final String setterName = "set" + capitalize( field.getName() );
		try {
			return getFieldClass().getDeclaredMethod(setterName, getFieldClass());
		} catch (Exception e) {
			return null;
		}
	}
	public String getName() {
		return name;
	}
	
	private static final String capitalize(String name) {
		if (name == null || name.length() == 0) {
			return name;
		}
		if (Character.isUpperCase(name.charAt(0))){
			return name;
		}
		char chars[] = name.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return new String(chars);
		
	}

	public Field getField() {
		return field;
	}	
	
	public FieldMapper getFieldMapper() throws NotsupportedTypeException {
		FieldMapper mapper = FieldMapperRegister.getMapperForClass(getFieldClass());
		if (mapper == null) {
			throw new NotsupportedTypeException("Could not find mapper for type " + getFieldClass());
		} else {
			return mapper;
		}
	}
}
