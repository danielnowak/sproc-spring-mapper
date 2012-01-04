package com.typemapper.core.fieldMapper;

import org.apache.log4j.Logger;

public class IntFieldMapper implements FieldMapper {

	private static final Logger LOG = Logger.getLogger(IntFieldMapper.class);

	@Override
	public Object mapField(String string, Class clazz) {
		if (string == null) {
			return 0;
		}
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			LOG.error("Could not convert " + string + " to int.", e);
		}
		return 0;
	}
}
