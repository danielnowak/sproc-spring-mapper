package com.typemapper.core.fieldMapper;

import org.apache.log4j.Logger;

import com.typemapper.parser.exception.ParserException;
import com.typemapper.parser.postgres.ParseUtils;

public class BooleanFieldMapper implements FieldMapper {

	private static final Logger LOG = Logger.getLogger(CharFieldMapper.class);

	@Override
	public Object mapField(String string, Class clazz) {
		try {
			return ParseUtils.getBoolean(string);
		} catch (ParserException e) {
			LOG.error(String.format("Could not convert [%s] to Boolean.", string), e);
			return null;
		}
	}

}
