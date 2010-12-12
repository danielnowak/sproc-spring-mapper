package com.typemapper.core.fieldMapper;

import org.apache.log4j.Logger;

public class FloatFieldMapper implements FieldMapper {
	
	private static final Logger LOG = Logger.getLogger(FloatFieldMapper.class);
	

	@Override
	public Object mapField(String string) {
		try {
			return Float.parseFloat(string);
		} catch (NumberFormatException e) {
			LOG.error("Could not convert " + string + " to float.", e);
		}
		return null;
	}

}
