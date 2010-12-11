package com.typemapper.core;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class TypeMapper implements ParameterizedRowMapper {
	
	private static final Logger LOG = Logger.getLogger(TypeMapper.class);
	
	@SuppressWarnings("unchecked")
	private final Class resultClass;
	private final List<Mapping> mappings;
	
	@SuppressWarnings("unchecked")
	TypeMapper(Class resultClass) {
		this.resultClass = resultClass;
		mappings = Mapping.getMappingsForClass(this.resultClass);
	}

	@Override
	public Object mapRow(ResultSet set, int count) throws SQLException {
		Object result = null;
		try {
			result = getResultClass().newInstance();
			
			LOG.debug(set.toString());
			String stringResult = set.getString(1);
			LOG.debug(stringResult);
			fillObject(result, set);
		} catch (InstantiationException e) {
			throw new SQLException(getResultClass() + " has not public no arch constructor", e);
		} catch (IllegalAccessException e) {
			throw new SQLException(getResultClass() + " has not public no arch constructor", e);
		}
		return result;
	}
	
	private void fillObject(Object result, ResultSet set) throws SQLException {
		for (Mapping mapping :getMappings()) {
			try {
				String fieldStringValue = set.getString(mapping.getName());
				Object value = mapping.getFieldMapper().mapField(fieldStringValue);
				Method setter = mapping.getSetter(); 
				if (setter != null) {
					setter.invoke(result, value);
				} else {
					mapping.getField().set(result, value);
				}
			} catch (Exception e) {
				LOG.error(e, e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Class getResultClass() {
		return resultClass;
	}

	public List<Mapping> getMappings() {
		return mappings;
	}

}
