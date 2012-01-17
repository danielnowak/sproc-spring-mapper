package com.typemapper.namedresult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.typemapper.AbstractTest;

import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;

import com.typemapper.namedresult.results.ClassWithMap;
import org.junit.Ignore;

public class HStoreTest extends AbstractTest {

    @Test
    //@Ignore("Needs hstore() installed into postgres DB")
    public void testHStoreNull() throws SQLException {
        TypeMapperFactory.initTypeAndFunctionCaches(connection, "default");

        final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, null as map");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithMap.class);
        int i = 0;
        while (rs.next()) {
            ClassWithMap result = (ClassWithMap) mapper.mapRow(rs, i++);
            Assert.assertEquals("str", result.getStr());
            Assert.assertNull(result.getMap());
        }
    }
    
    @Test
    //@Ignore("Needs hstore() installed into postgres DB")
    public void testHStoreFilled() throws SQLException {
        TypeMapperFactory.initTypeAndFunctionCaches(connection, "default");

        final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, hstore('key', 'val') as map");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithMap.class);
        int i = 0;
        while (rs.next()) {
            ClassWithMap result = (ClassWithMap) mapper.mapRow(rs, i++);
            Assert.assertEquals("str", result.getStr());
            Assert.assertNotNull(result.getMap());
            Assert.assertEquals("key", result.getMap().keySet().iterator().next());
            Assert.assertEquals("val", result.getMap().values().iterator().next());
        }
    }

}
