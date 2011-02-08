package com.typemapper.namedresult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.typemapper.AbstractTest;
import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;
import com.typemapper.namedresult.results.ClassWithEnum;
import com.typemapper.namedresult.results.SampleEnum;

public class EnumTest extends AbstractTest {

    @Test
    public void testEnumMappings() throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("SELECT 'MALE' AS male_female");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithEnum.class);
        int i = 0;
        while( rs.next() ) {
            ClassWithEnum result = (ClassWithEnum) mapper.mapRow(rs, i++);
            Assert.assertNotNull(result);
            Assert.assertEquals(SampleEnum.MALE, result.getSampleEnum());
        }
    }
    
}
