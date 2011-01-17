package com.typemapper.core.fieldMapper;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.typemapper.core.result.ArrayResultNode;
import com.typemapper.core.result.DbResultNode;
import com.typemapper.core.result.ObjectResultNode;
import com.typemapper.exception.NotsupportedTypeException;

public class ArrayFieldMapper {
	
	private static final Logger LOG = Logger.getLogger(ArrayFieldMapper.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object mapField(Field field, ArrayResultNode node) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotsupportedTypeException {
		Collection result = null;
		if (field.getType().isAssignableFrom(List.class)) {
			result = new ArrayList();
		}
		for (DbResultNode child : node.getChildren()) {
			ParameterizedType type = (ParameterizedType) field.getGenericType();
			Type[] actualTypeArguments = type.getActualTypeArguments();
			Object obj = ObjectFieldMapper.mapField((Class) actualTypeArguments[0], (ObjectResultNode) child);
			result.add(obj);
		}
		
		LOG.info(node);
		LOG.info(field);
		return result;
	}

}
