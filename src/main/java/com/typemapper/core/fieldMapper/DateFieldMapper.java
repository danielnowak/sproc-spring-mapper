package com.typemapper.core.fieldMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

public class DateFieldMapper implements FieldMapper {
	
	private static final Logger LOG = Logger.getLogger(DateFieldMapper.class); 

	@Override
	public Object mapField(String string, Class clazz) {
		try {
			return new SimpleDateFormat("yyyy-mm-dd k:m:s").parse(string);
		} catch (ParseException e) {
			LOG.error("Could not parse date: " + string, e);
			return null;
		}
	}

}
