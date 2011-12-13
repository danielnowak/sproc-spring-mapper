package com.typemapper.core.fieldMapper;

import java.math.BigDecimal;

public class BigDecimalFieldMappper implements FieldMapper {

	@Override
	public Object mapField(String string, Class clazz) {
		return new BigDecimal(string);
	}

}
