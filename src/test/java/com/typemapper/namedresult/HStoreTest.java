package com.typemapper.namedresult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import org.springframework.util.CollectionUtils;

import com.typemapper.AbstractTest;

import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;

import com.typemapper.namedresult.results.ClassWithClassWithListOfMap;
import com.typemapper.namedresult.results.ClassWithClassWithMap;
import com.typemapper.namedresult.results.ClassWithListOfMap;
import com.typemapper.namedresult.results.ClassWithMap;

public class HStoreTest extends AbstractTest {

    @Test
    // @Ignore("Needs hstore() installed into postgres DB")
    public void testHStoreNull() throws SQLException {
        TypeMapperFactory.initTypeAndFunctionCaches(connection, "default");

        final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, null as map");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper<?> mapper = TypeMapperFactory.createTypeMapper(ClassWithMap.class);
        int i = 0;
        while (rs.next()) {
            final ClassWithMap result = (ClassWithMap) mapper.mapRow(rs, i++);
            Assert.assertEquals("str", result.getStr());
            Assert.assertNull(result.getMap());
        }
    }

    @Test
    // @Ignore("Needs hstore() installed into postgres DB")
    public void testHStoreFilled() throws SQLException {
        TypeMapperFactory.initTypeAndFunctionCaches(connection, "default");

        final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, hstore('key', 'val') as map");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper<?> mapper = TypeMapperFactory.createTypeMapper(ClassWithMap.class);
        int i = 0;
        while (rs.next()) {
            final ClassWithMap result = (ClassWithMap) mapper.mapRow(rs, i++);
            Assert.assertEquals("str", result.getStr());
            Assert.assertNotNull(result.getMap());
            Assert.assertEquals("key", result.getMap().keySet().iterator().next());
            Assert.assertEquals("val", result.getMap().values().iterator().next());
        }
    }

    @Test
    // @Ignore("Needs hstore() installed into postgres DB")
    public void testHStoreType() throws SQLException {
        TypeMapperFactory.initTypeAndFunctionCaches(connection, "default");

        final PreparedStatement ps = connection.prepareStatement("SELECT tmp.hstore_type_function();");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper<?> mapper = TypeMapperFactory.createTypeMapper(ClassWithClassWithMap.class);
        int i = 0;
        while (rs.next()) {
            final ClassWithClassWithMap firstResult = (ClassWithClassWithMap) mapper.mapRow(rs, i++);
            final ClassWithMap result = firstResult.getClassWithMap();
            Assert.assertEquals("str", result.getStr());
            Assert.assertNotNull(result.getMap());
            Assert.assertEquals("key", result.getMap().keySet().iterator().next());
            Assert.assertEquals("val", result.getMap().values().iterator().next());
        }
    }

    @Test
    // @Ignore("Needs hstore() installed into postgres DB")
    public void testHStoreArrayType() throws SQLException {
        TypeMapperFactory.initTypeAndFunctionCaches(connection, "default");

        final PreparedStatement ps = connection.prepareStatement("SELECT tmp.hstore_array_type_function();");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper<?> mapper = TypeMapperFactory.createTypeMapper(ClassWithClassWithListOfMap.class);
        int i = 0;
        while (rs.next()) {
            final ClassWithClassWithListOfMap firstResult = (ClassWithClassWithListOfMap) mapper.mapRow(rs, i++);
            final ClassWithListOfMap result = firstResult.getClassWithListOfMap();
            final List<Map<String, String>> mapList = result.getMapList();
            Assert.assertNotNull("List is Null", mapList);
            Assert.assertFalse("List is Empty", CollectionUtils.isEmpty(mapList));
            Assert.assertEquals("str", result.getStr());
            Assert.assertNotNull(result.getMapList().get(0));
            Assert.assertEquals("key", result.getMapList().get(0).keySet().iterator().next());
            Assert.assertEquals("val", result.getMapList().get(0).values().iterator().next());
        }
    }

}
