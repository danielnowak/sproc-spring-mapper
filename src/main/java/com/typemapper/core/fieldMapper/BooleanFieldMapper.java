package com.typemapper.core.fieldMapper;

import com.typemapper.parser.postgres.ParseUtils;

public class BooleanFieldMapper implements FieldMapper {

	@Override
	public Object mapField(String string) {
		return ParseUtils.getBoolean(string);
	}

}
