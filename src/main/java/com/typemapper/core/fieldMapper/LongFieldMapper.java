package com.typemapper.core.fieldMapper;

import org.apache.log4j.Logger;

public class LongFieldMapper implements FieldMapper {
	
	private static final Logger LOG = Logger.getLogger(LongFieldMapper.class);

	@Override
	public Object mapField(String string, Class clazz) {
		try {
			return Long.parseLong(string);
		} catch (NumberFormatException e) {
			LOG.error(String.format("Could not convert [%s] to long.", string), e);
		}
		return null;
	}


}
