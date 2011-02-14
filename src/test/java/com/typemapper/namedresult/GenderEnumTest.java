package com.typemapper.namedresult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.typemapper.AbstractTest;
import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;
import com.typemapper.namedresult.results.ClassWithGenderEnum;
import com.typemapper.namedresult.results.GenderEnum;

public class GenderEnumTest extends AbstractTest {

    @Test
    public void testEnumMappings() throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("SELECT 'MALE' AS male_female");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithGenderEnum.class);
        int i = 0;
        while( rs.next() ) {
            ClassWithGenderEnum result = (ClassWithGenderEnum) mapper.mapRow(rs, i++);
            Assert.assertNotNull(result);
            Assert.assertEquals(GenderEnum.MALE, result.getGender());
        }
    }
    
}
