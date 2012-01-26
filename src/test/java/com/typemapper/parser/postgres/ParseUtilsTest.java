package com.typemapper.parser.postgres;

import java.util.List;

import org.junit.Test;

import com.typemapper.parser.exception.RowParserException;

import junit.framework.Assert;

/**
 * @author  hjacobs
 */
public class ParseUtilsTest {

    @Test
    public void testPostgresRow2StringListSimple() throws RowParserException {

        List<String> fieldValues;
        fieldValues = ParseUtils.postgresROW2StringList("(a,b)");
        Assert.assertEquals(2, fieldValues.size());
        Assert.assertEquals("a", fieldValues.get(0));
        Assert.assertEquals("b", fieldValues.get(1));

        fieldValues = ParseUtils.postgresROW2StringList("(a,)");
        Assert.assertEquals(2, fieldValues.size());
        Assert.assertEquals("a", fieldValues.get(0));
        Assert.assertNull(fieldValues.get(1));

        fieldValues = ParseUtils.postgresROW2StringList("(,b)");
        Assert.assertEquals(2, fieldValues.size());
        Assert.assertNull(fieldValues.get(0));
        Assert.assertEquals("b", fieldValues.get(1));

        fieldValues = ParseUtils.postgresROW2StringList("(,)");
        Assert.assertEquals(2, fieldValues.size());
        Assert.assertNull(fieldValues.get(0));
        Assert.assertNull(fieldValues.get(1));

        fieldValues = ParseUtils.postgresROW2StringList("(a,,,b)");
        Assert.assertEquals(4, fieldValues.size());
        Assert.assertEquals("a", fieldValues.get(0));
        Assert.assertNull(fieldValues.get(1));
        Assert.assertNull(fieldValues.get(2));
        Assert.assertEquals("b", fieldValues.get(3));
    }

    @Test
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
