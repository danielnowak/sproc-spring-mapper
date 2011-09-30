package com.typemapper.core.fieldMapper;

import org.apache.log4j.Logger;

public class EnumrationFieldMapper implements FieldMapper {
	
	private static final Logger LOG = Logger.getLogger(EnumrationFieldMapper.class); 

	@SuppressWarnings("rawtypes")
	@Override
	public Object mapField(String string, Class clazz) {
		if (clazz.getEnumConstants() == null) {
			LOG.warn( clazz + " is not an enum");
			return null;
		}
		Enum[] enumConstants = (Enum[]) clazz.getEnumConstants();
		try {
			int enumValue = Integer.parseInt(string);
			for (Enum e : enumConstants ) {
				if (e.ordinal() == enumValue) {
					return e;
				}
			}
			LOG.warn("Could not find enum in " + clazz + " with ordinal " + string);
		} catch (NumberFormatException e) {
			for (Enum en : enumConstants ) {
				if (en.name().equals(string)) {
					return en;
				}
			}
			LOG.warn("Could not find enum in " + clazz + " with name " + string);
		}
		return null;
	}

}
