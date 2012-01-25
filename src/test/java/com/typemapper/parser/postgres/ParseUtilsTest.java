package com.typemapper.parser.postgres;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.typemapper.parser.exception.RowParserException;

import junit.framework.Assert;

/**
 * @author  hjacobs
 */
public class ParseUtilsTest {

    @Test
    @Ignore("TODO: This test fails because postgresROW2StringList is BUGGY!")
    public void testPostgresRow2StringList() throws RowParserException {

        List<String> fieldValues;
        fieldValues = ParseUtils.postgresROW2StringList("(,,0,1)");
        Assert.assertEquals(4, fieldValues.size());
        Assert.assertNull(fieldValues.get(0));
        Assert.assertNull(fieldValues.get(1));
        Assert.assertEquals("0", fieldValues.get(2));
        Assert.assertEquals("1", fieldValues.get(3));
    }

}
