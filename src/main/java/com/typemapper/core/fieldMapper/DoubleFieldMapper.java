package com.typemapper.core.fieldMapper;

import org.apache.log4j.Logger;

public class DoubleFieldMapper implements FieldMapper {

	private static final Logger LOG = Logger.getLogger(DoubleFieldMapper.class);

	@Override
	public Object mapField(String string, Class clazz) {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException e) {
			LOG.error("Could not convert " + string + " to double.", e);
		}
		return null;
	}
}
