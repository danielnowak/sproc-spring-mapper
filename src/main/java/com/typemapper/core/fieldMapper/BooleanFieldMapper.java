package com.typemapper.core.fieldMapper;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.typemapper.parser.exception.ParserException;
import com.typemapper.parser.postgres.ParseUtils;

public class BooleanFieldMapper implements FieldMapper {

	private static final Logger LOG = Logger.getLogger(CharFieldMapper.class);

	@Override
	public Object mapField(String string, Class clazz) {
		if (string == null) {
			return null;
		}
		try {
			return ParseUtils.getBoolean(string);
		} catch (ParserException e) {
			LOG.error("Could not convert " + string + " to Boolean.");
			return null;
		}
	}

}
