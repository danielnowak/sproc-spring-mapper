package com.typemapper.core.fieldMapper;

import java.math.BigDecimal;

public class BigDecimalFieldMappper implements FieldMapper {

	@Override
	public Object mapField(String string, Class clazz) {
		if (string == null) {
			return null;
		}
		return new BigDecimal(string);
	}

}
