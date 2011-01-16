package com.typemapper.core.fieldMapper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.typemapper.core.Mapping;
import com.typemapper.core.result.DbResultNode;
import com.typemapper.core.result.DbResultNodeType;
import com.typemapper.core.result.ObjectResultNode;
import com.typemapper.exception.NotsupportedTypeException;

public class ObjectFieldMapper {
	
	public final static Object mapField(@SuppressWarnings("rawtypes") Class clazz, ObjectResultNode node) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotsupportedTypeException {
		List<Mapping> mappings = Mapping.getMappingsForClass(clazz);
		Object result = clazz.newInstance();
		
		for (Mapping mapping: mappings) {
			DbResultNode currentNode = node.getChildByName(mapping.getName());
			if (DbResultNodeType.SIMPLE.equals(currentNode.getNodeType())) {
				mapping.map(result, mapping.getFieldMapper().mapField(currentNode.getValue()));
			} else if (DbResultNodeType.OBJECT.equals(currentNode.getNodeType())) {
				mapping.map(result, mapField(mapping.getFieldClass(), (ObjectResultNode) currentNode));
			}
		}
		
		return result;
	}

}
