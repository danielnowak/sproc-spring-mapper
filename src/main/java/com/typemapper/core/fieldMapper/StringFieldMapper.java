package com.typemapper.core.fieldMapper;

public class StringFieldMapper implements FieldMapper {

	@Override
	public Object mapField(String string) {
		return string;
	}

}
