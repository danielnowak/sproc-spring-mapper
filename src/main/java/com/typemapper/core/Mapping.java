package com.typemapper.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.typemapper.annotations.DatabaseField;
import com.typemapper.annotations.Embed;
import com.typemapper.core.fieldMapper.AnyTransformer;
import com.typemapper.core.fieldMapper.FieldMapper;
import com.typemapper.core.fieldMapper.FieldMapperRegister;
import com.typemapper.core.fieldMapper.ValueTransformerFieldMapper;
import com.typemapper.exception.NotsupportedTypeException;


public class Mapping {
	
	private final String name;
	private final Class<? extends ValueTransformer<String, ?>> valueTransformer;
	private final Field field;
	private final boolean embed;
	private final Field embedField;
	@SuppressWarnings("rawtypes")
	private static final Map<Class,List<Mapping>> cache = new HashMap<Class, List<Mapping>>(); 
	
	public static List<Mapping> getMappingsForClass(@SuppressWarnings("rawtypes") final Class clazz) {
		List<Mapping> result = cache.get(clazz);
		if (result == null) {
			result = getMappingsForClass(clazz, false, null);
			cache.put(clazz, result);
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	static List<Mapping> getMappingsForClass( final Class clazz, final boolean embed, final Field embedField) {
		final List<Mapping> result = new ArrayList<Mapping>();
		final List<Field> fields = new LinkedList<Field>();
		for (final Field field : clazz.getDeclaredFields()) {
			fields.add(field);
		}		
		Class parentClass = clazz.getSuperclass();
		while (parentClass != null) {
			for (final Field field : parentClass.getDeclaredFields()) {
				fields.add(field);
			}
			parentClass = parentClass.getSuperclass();
		}
		
		for (final Field field : fields) {
			final DatabaseField annotation = field.getAnnotation(DatabaseField.class);
			if (annotation != null) {
				result.add(new Mapping(field, annotation.name(), embed, embedField, annotation.transformer()));
			}
			if (!embed) {
				final Embed embedAnnotation = field.getAnnotation(Embed.class);
				if (embedAnnotation != null) {
					result.addAll(getMappingsForClass(field.getType(), true, field));
				}
			}
		}
		return result;
	}
	
	Mapping(final Field field, final String name, final boolean embed, final Field embedField,
	        final Class<? extends ValueTransformer<String, ?>> valueTransformer) {
		this.name = name;
		this.field = field;
		this.embed = embed;
		this.embedField = embedField;
		this.valueTransformer = valueTransformer;
	}
	
	@SuppressWarnings("rawtypes")
	public Class getFieldClass() {
		return field.getType();
	}
	
	public Class<? extends ValueTransformer<String, ?>> getValueTransformer() {
        return valueTransformer;
    }

    public Method getSetter(final Field field) {
		final String setterName = "set" + capitalize( field.getName() );
		try {
			return field.getDeclaringClass().getDeclaredMethod(setterName, field.getType());
		} catch (final Exception e) {
			return null;
		}
	}
	
	public Method getSetter() {
		return getSetter(field);
	}
	
	public Method getGetter(final Field field) {
		final String getterName = "get" + capitalize( field.getName() );
		try {
			return field.getDeclaringClass().getDeclaredMethod(getterName);
		} catch (final Exception e) {
			return null;
		}

	}
	
	public String getName() {
		return name;
	}
	
	private static final String capitalize(final String name) {
		if (name == null || name.length() == 0) {
			return name;
		}
		if (Character.isUpperCase(name.charAt(0))){
			return name;
		}
		final char chars[] = name.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return new String(chars);
		
	}

	public Field getField() {
		return field;
	}	
	
	public FieldMapper getFieldMapper() throws NotsupportedTypeException, InstantiationException,
	    IllegalAccessException {
		FieldMapper mapper = FieldMapperRegister.getMapperForClass(getFieldClass());
		if (mapper == null) {
		    if (!AnyTransformer.class.equals(getValueTransformer())) {
                mapper = new ValueTransformerFieldMapper(getValueTransformer());
            }
		    if (mapper == null) {
		        throw new NotsupportedTypeException("Could not find mapper for type " + getFieldClass());
            }
		}
		return mapper;
	}

	public void map(final Object target, final Object value) throws IllegalArgumentException, IllegalAccessException,
	    InvocationTargetException, InstantiationException {
		if (embed) {
			Object embedValue = getEmbedFieldValue(target);
			if (embedValue == null) {
				embedValue = initEmbed(target);
			}
			final Method setter = getSetter(); 
			if (setter != null) {
				setter.invoke(embedValue, value);
			} else {
				getField().set(embedValue, value);
			}
		} else {
			final Method setter = getSetter(); 
			if (setter != null) {
				setter.invoke(target, value);
			} else {
				getField().set(target, value);
			}
			
		}
	}

	private Object initEmbed(final Object target) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		final Method setter = getSetter(embedField); 
		final Object value = embedField.getType().newInstance();
		if (setter != null) {
			setter.invoke(target, value);
		} else {
			getField().set(target, value);
		}
		return value;
		
	}

	private Object getEmbedFieldValue(final Object target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		final Method setter = getGetter(embedField);
		Object result = null;
		if (setter != null) {
			result = setter.invoke(target);
		} else {
			result = embedField.get(target);
		}
		return result;
	}

	@Override
	public String toString() {
		return "Mapping [name=" + name + ", field=" + field + ", embed="
				+ embed + ", embedField=" + embedField + "]";
	}
}
