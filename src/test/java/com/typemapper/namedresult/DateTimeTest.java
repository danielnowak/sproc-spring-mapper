package com.typemapper.namedresult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.typemapper.AbstractTest;

import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;

import com.typemapper.namedresult.results.ClassWithDateTime;

public class DateTimeTest extends AbstractTest {

    @Test
    public void testDateTimeMappings() throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("SELECT lt, gt, zone from tmp.test_time");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper<?> mapper = TypeMapperFactory.createTypeMapper(ClassWithDateTime.class);
        int i = 0;
        while (rs.next()) {
            final ClassWithDateTime result = (ClassWithDateTime) mapper.mapRow(rs, i++);
            Assert.assertNotNull(result);
            Assert.assertEquals(result.getDateWithTimezone(), result.getDateWithoutTimezone());
        }
    }
}
