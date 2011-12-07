package com.typemapper.core.fieldMapper;

import org.apache.log4j.Logger;

public class PrimitiveLongFieldMapper implements FieldMapper {

	private static final Logger LOG = Logger.getLogger(PrimitiveLongFieldMapper.class);
	
	@Override
	public Object mapField(String string, Class clazz) {
		try {
			return Long.parseLong(string);
		} catch (NumberFormatException e) {
			LOG.error(String.format("Could not convert [%s] to long.", string), e);
		}
		return Long.valueOf(0);
	}

}
