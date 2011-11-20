package com.typemapper.core.fieldMapper;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

public class DateFieldMapperValuesTest {

	private static final DateFieldMapper MAPPER = new DateFieldMapper();

	private static final String DATE_STRING = "2011-11-11 11:11:11";

	@Test
	public void testHourAndMinute() throws Exception {
		Date result = (Date) MAPPER.mapField(DATE_STRING, Date.class);
		assertEquals(DATE_STRING,
				new SimpleDateFormat("yyyy-MM-dd k:m:s").format(result));
	}

	@Test
	public void wrongInputShouldYieldNull() throws Exception {
		assertNull(MAPPER.mapField("foo", Date.class));
	}

	@Test
	public void nullClassDoesNotBorkMethod() throws Exception {
		Object result = MAPPER.mapField(DATE_STRING, null);
		assertNotNull(result);
		assertThat("util date", result, instanceOf(Date.class));
	}

	@Test
	public void wrongClassDoesNotBorkMethod() throws Exception {
		Object result = MAPPER.mapField(DATE_STRING, String.class);
		assertNotNull(result);
		assertThat("util date", result, instanceOf(Date.class));
	}

	@Test
	public void sqlDateClassReturnsCorrectObject() throws Exception {
		Object result = MAPPER.mapField(DATE_STRING, java.sql.Date.class);
		assertNotNull(result);
		assertThat("sql date", result, instanceOf(java.sql.Date.class));
	}
	
	@Test
	public void returnsNullOnNullOrEmpty() throws Exception {
		Object result = MAPPER.mapField(null, Date.class);
		assertNull("on null", result);

		result = MAPPER.mapField("", Date.class);
		assertNull("on empty", result);
	}

	@Test
	public void jodaTimeDateClassReturnsCorrectObject() throws Exception {
		Object result = MAPPER.mapField(DATE_STRING, DateTime.class);
		assertNotNull(result);
		assertThat("joda time", result, instanceOf(DateTime.class));
	}
}
