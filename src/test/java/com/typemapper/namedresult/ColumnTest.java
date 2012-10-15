package com.typemapper.namedresult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.typemapper.AbstractTest;

import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;

import com.typemapper.namedresult.results.ClassWithColumnDefinition;

public class ColumnTest extends AbstractTest {

    @Test
    public void testColumnMappings() throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("SELECT 2 as l, 'c' as c");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper<?> mapper = TypeMapperFactory.createTypeMapper(ClassWithColumnDefinition.class);
        int i = 0;
        while (rs.next()) {
            final ClassWithColumnDefinition result = (ClassWithColumnDefinition) mapper.mapRow(rs, i++);
            Assert.assertNotNull(result);
            Assert.assertEquals(2, result.getL());
            Assert.assertEquals('c', result.getC());
        }
    }
}
