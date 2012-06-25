package com.typemapper.namedresult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Assert;
import org.junit.Test;

import org.postgresql.util.PSQLException;

import com.typemapper.AbstractTest;

import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;

import com.typemapper.namedresult.results.ClassWithListOfEnums;
import com.typemapper.namedresult.results.Enumeration;

public class CollectionWithEnumsTest extends AbstractTest {

    @Test
    public void testListWithEnums() throws Exception {
        final PreparedStatement ps = connection.prepareStatement(
                "SELECT 'str' as str, ARRAY['VALUE_1'::enumeration, 'VALUE_2'::enumeration]::enumeration[] as enum_arr");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper<ClassWithListOfEnums> mapper = TypeMapperFactory.createTypeMapper(ClassWithListOfEnums.class);
        int i = 0;
        while (rs.next()) {
            final ClassWithListOfEnums result = (ClassWithListOfEnums) mapper.mapRow(rs, i++);
            Assert.assertNotNull(result);
            Assert.assertNotNull(result.getEnumList());
            Assert.assertTrue(result.getEnumList().size() == 2);
            Assert.assertNotNull(result.getEnumList().get(0));
            Assert.assertNotNull(result.getEnumList().get(1));

            final Enumeration enum1 = result.getEnumList().get(0);
            Assert.assertEquals(Enumeration.VALUE_1, enum1);

            final Enumeration enum2 = result.getEnumList().get(1);
            Assert.assertEquals(Enumeration.VALUE_2, enum2);
            Assert.assertEquals("str", result.getStr());
        }
    }

    @Test
    public void testListWithEmptyEnums() throws Exception {
        final PreparedStatement ps = connection.prepareStatement(
                "SELECT 'str' as str, ARRAY[]::enumeration[] as enum_arr");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper<ClassWithListOfEnums> mapper = TypeMapperFactory.createTypeMapper(ClassWithListOfEnums.class);
        int i = 0;
        while (rs.next()) {
            final ClassWithListOfEnums result = (ClassWithListOfEnums) mapper.mapRow(rs, i++);
            Assert.assertNotNull(result);
            Assert.assertNotNull(result.getEnumList());
            Assert.assertTrue(result.getEnumList().size() == 0);
        }
    }

    @Test
    public void testListWithEmptyEnumOrdinals() throws Exception {
        final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, ARRAY[0, 1] as enum_arr");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper<ClassWithListOfEnums> mapper = TypeMapperFactory.createTypeMapper(ClassWithListOfEnums.class);
        int i = 0;
        while (rs.next()) {
            final ClassWithListOfEnums result = (ClassWithListOfEnums) mapper.mapRow(rs, i++);
            Assert.assertNotNull(result);
            Assert.assertNotNull(result.getEnumList());
            Assert.assertTrue(result.getEnumList().size() == 2);
            Assert.assertNotNull(result.getEnumList().get(0));
            Assert.assertNotNull(result.getEnumList().get(1));

            final Enumeration enum1 = result.getEnumList().get(0);
            Assert.assertEquals(Enumeration.VALUE_1, enum1);

            final Enumeration enum2 = result.getEnumList().get(1);
            Assert.assertEquals(Enumeration.VALUE_2, enum2);
            Assert.assertEquals("str", result.getStr());
        }
    }

    @Test
    public void testListWithNullEnum() throws Exception {
        final PreparedStatement ps = connection.prepareStatement(
                "SELECT 'str' as str, ARRAY[NULL::enumeration, 'VALUE_2'::enumeration]::enumeration[] as enum_arr");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper<ClassWithListOfEnums> mapper = TypeMapperFactory.createTypeMapper(ClassWithListOfEnums.class);
        int i = 0;
        while (rs.next()) {
            final ClassWithListOfEnums result = (ClassWithListOfEnums) mapper.mapRow(rs, i++);
            Assert.assertNotNull(result);
            Assert.assertNotNull(result.getEnumList());
            Assert.assertTrue(result.getEnumList().size() == 2);
            Assert.assertNull(result.getEnumList().get(0));
            Assert.assertNotNull(result.getEnumList().get(1));

            final Enumeration enum2 = result.getEnumList().get(1);
            Assert.assertEquals(Enumeration.VALUE_2, enum2);
            Assert.assertEquals("str", result.getStr());
        }
    }

    @Test
    public void testListWithNullEnum2() throws Exception {
        final PreparedStatement ps = connection.prepareStatement(
                "SELECT 'str' as str, ARRAY[NULL, 'VALUE_2'::enumeration] as enum_arr");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper<ClassWithListOfEnums> mapper = TypeMapperFactory.createTypeMapper(ClassWithListOfEnums.class);
        int i = 0;
        while (rs.next()) {
            final ClassWithListOfEnums result = (ClassWithListOfEnums) mapper.mapRow(rs, i++);
            Assert.assertNotNull(result);
            Assert.assertNotNull(result.getEnumList());
            Assert.assertTrue(result.getEnumList().size() == 2);
            Assert.assertNull(result.getEnumList().get(0));
            Assert.assertNotNull(result.getEnumList().get(1));

            final Enumeration enum2 = result.getEnumList().get(1);
            Assert.assertEquals(Enumeration.VALUE_2, enum2);
            Assert.assertEquals("str", result.getStr());
        }
    }

    @Test
    public void testListWithNullEnum3() throws Exception {
        final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, ARRAY[NULL] as enum_arr");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper<ClassWithListOfEnums> mapper = TypeMapperFactory.createTypeMapper(ClassWithListOfEnums.class);
        int i = 0;
        while (rs.next()) {
            final ClassWithListOfEnums result = (ClassWithListOfEnums) mapper.mapRow(rs, i++);
            Assert.assertNotNull(result);
            Assert.assertNotNull(result.getEnumList());
            Assert.assertTrue(result.getEnumList().size() == 1);
            Assert.assertNull(result.getEnumList().get(0));
        }
    }

    @Test(expected = PSQLException.class)
    public void testListWithInvalidEnums() throws Exception {
        final PreparedStatement ps = connection.prepareStatement(
                "SELECT 'str' as str, ARRAY['VALUE_3'::enumeration]::enumeration[] as enum_arr");
        ps.executeQuery();
    }
}
