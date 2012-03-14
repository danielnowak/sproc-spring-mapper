package com.typemapper.core.fieldMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.typemapper.core.result.ArrayResultNode;
import com.typemapper.core.result.DbResultNode;
import com.typemapper.core.result.ObjectResultNode;
import com.typemapper.core.result.SimpleResultNode;
import com.typemapper.exception.NotsupportedTypeException;

public class ArrayFieldMapper {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object mapField(Field field, ArrayResultNode node) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotsupportedTypeException {
		Collection result = null;
		if (field.getType().isAssignableFrom(List.class)) {
			result = new ArrayList();
		} else if (field.getType().isAssignableFrom(Set.class)) {
			result = new HashSet();
		}
		for (DbResultNode child : node.getChildren()) {
			ParameterizedType type = (ParameterizedType) field.getGenericType();
			Type[] actualTypeArguments = type.getActualTypeArguments();
			Object obj = null;
			if (child instanceof ObjectResultNode) {
				obj = ObjectFieldMapper.mapField((Class) actualTypeArguments[0], (ObjectResultNode) child);
			} else if (child instanceof SimpleResultNode) {
                FieldMapper mapperForClass;
				if(actualTypeArguments[0] instanceof ParameterizedType){
                    mapperForClass = FieldMapperRegister.getMapperForClass((Class) ((ParameterizedType) actualTypeArguments[0]).getRawType());
                    obj = mapperForClass.mapField(child.getValue(), (Class) ((ParameterizedType) actualTypeArguments[0]).getRawType());
                } else {
                    mapperForClass = FieldMapperRegister.getMapperForClass((Class) actualTypeArguments[0]);
                    obj = mapperForClass.mapField(child.getValue(), (Class) actualTypeArguments[0]);
                }
			}
			result.add(obj);
		}
		return result;
	}

}
