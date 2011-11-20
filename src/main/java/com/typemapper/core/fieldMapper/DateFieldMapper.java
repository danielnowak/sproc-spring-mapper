package com.typemapper.core.fieldMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

public class DateFieldMapper implements FieldMapper {

	private static final Logger LOG = Logger.getLogger(DateFieldMapper.class);

	private static final String[] FORMATS = { "yyyy-MM-dd k:m:s",
			"yyyy-MM-dd k:m", "yyyy-MM-dd" };

	@Override
	public Object mapField(String string, Class clazz) {
		if (string == null || string.isEmpty()) {
			return null;
		}
		Date result = null;
		for (final String format : FORMATS) {
			try {
				result = new SimpleDateFormat(format).parse(string);
				if (result != null) {
					break;
				}
			} catch (ParseException e) {
			}
		}
		if (result == null) {
			LOG.error("Could not parse date: " + string);
		}
		if (clazz != null && clazz.equals(DateTime.class) && result != null) {
			return new DateTime(result.getTime());
		} else if (clazz != null && clazz.equals(java.sql.Date.class) && result != null) {
			return new java.sql.Date(result.getTime());
		} else {
			return result;
		}
	}

}
