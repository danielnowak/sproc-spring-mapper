package com.typemapper.core.fieldMapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.SimpleDateFormat;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class DateFieldMapperValuesTest {

    private static final DateFieldMapper MAPPER = new DateFieldMapper();

    private static final String DATE_STRING = "2011-11-11 11:11:11";

    @Test
    public void testHourAndMinute() throws Exception {
        final Date result = (Date) MAPPER.mapField(DATE_STRING, Date.class);
        assertEquals(DATE_STRING, new SimpleDateFormat("yyyy-MM-dd k:m:s").format(result));
    }

    @Test
    public void wrongInputShouldYieldNull() throws Exception {
        assertNull(MAPPER.mapField("foo", Date.class));
    }

    @Test
    public void nullClassDoesNotBorkMethod() throws Exception {
        final Object result = MAPPER.mapField(DATE_STRING, null);
        assertNotNull(result);
        Assert.assertTrue(Date.class.isAssignableFrom(result.getClass()));
    }

    @Test
    public void wrongClassDoesNotBorkMethod() throws Exception {
        final Object result = MAPPER.mapField(DATE_STRING, String.class);
        assertNotNull(result);
        Assert.assertTrue(Date.class.isAssignableFrom(result.getClass()));
    }

    @Test
    public void sqlDateClassReturnsCorrectObject() throws Exception {
        final Object result = MAPPER.mapField(DATE_STRING, java.sql.Date.class);
        assertNotNull(result);
        assertEquals(java.sql.Date.class, result.getClass());
    }
}
