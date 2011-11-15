package com.typemapper.core.fieldMapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Date;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class DateFieldMapperTest {

	@DataPoints
	public static final String[] DATES = { "2011-11-11 11:11:11",
			"2011-11-11 11:11", "2011-11-11" };

	@DataPoints
	public static final Class<?>[] CLASSES = { Date.class, java.util.Date.class };

	private static final DateFieldMapper MAPPER = new DateFieldMapper();

	@Theory
	public void parseAble(final String string, final Class<?> clazz) throws Exception {
		Object result = MAPPER.mapField(string, clazz);
		assertNotNull(result);
		assertEquals(result.getClass(), clazz);
	}
}
