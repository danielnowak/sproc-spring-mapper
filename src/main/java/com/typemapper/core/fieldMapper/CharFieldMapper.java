package com.typemapper.core.fieldMapper;

import org.apache.log4j.Logger;

public class CharFieldMapper implements FieldMapper {
	
	private static final Logger LOG = Logger.getLogger(CharFieldMapper.class);

	@Override
	public Object mapField(String string, Class clazz) {
		if (string == null) {
			return null;
		}
		if (string.length() == 1) {
			return string.charAt(0);
		} else {
			LOG.error("Could not convert " + string + " to char.");
		}
		return null;
	}

}
