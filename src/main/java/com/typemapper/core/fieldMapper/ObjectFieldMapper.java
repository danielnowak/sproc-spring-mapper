package com.typemapper.core.fieldMapper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;

import com.typemapper.core.Mapping;
import com.typemapper.core.result.ArrayResultNode;
import com.typemapper.core.result.DbResultNode;
import com.typemapper.core.result.DbResultNodeType;
import com.typemapper.core.result.ObjectResultNode;
import com.typemapper.exception.NotsupportedTypeException;

public class ObjectFieldMapper {
	
	private static final Logger LOG = Logger.getLogger(ObjectFieldMapper.class);
	
	public final static Object mapField(@SuppressWarnings("rawtypes") Class clazz, ObjectResultNode node) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotsupportedTypeException {
		if (node.getChildren() == null) {
			return null;
		}
		List<Mapping> mappings = Mapping.getMappingsForClass(clazz);
		Object result = clazz.newInstance();
		
		for (Mapping mapping: mappings) {
			DbResultNode currentNode = node.getChildByName(mapping.getName());
			if (currentNode == null) {
				LOG.warn("Could not find value of mapping: " + mapping.getName());
				continue;
			}
			if (DbResultNodeType.SIMPLE.equals(currentNode.getNodeType())) {
				mapping.map(result, mapping.getFieldMapper().mapField(currentNode.getValue(), mapping.getFieldClass()));
			} else if (DbResultNodeType.OBJECT.equals(currentNode.getNodeType())) {
				mapping.map(result, mapField(mapping.getFieldClass(), (ObjectResultNode) currentNode));
			} else if (DbResultNodeType.ARRAY.equals(currentNode.getNodeType())) {
				mapping.map(result, ArrayFieldMapper.mapField(mapping.getField(), (ArrayResultNode) currentNode));
			}
		}
		
		return result;
	}

}
