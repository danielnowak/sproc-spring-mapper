package com.typemapper.core.fieldMapper;

import org.apache.log4j.Logger;

public class ShortFieldMapper implements FieldMapper {
	
	private static final Logger LOG = Logger.getLogger(ShortFieldMapper.class);

	@Override
	public Object mapField(String string, Class clazz) {
		try {
			return Short.parseShort(string);
		} catch (NumberFormatException e) {
			LOG.error("Could not convert " + string + " to short.", e);
		}
		return null;
	}

}
