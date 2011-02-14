package com.typemapper.namedresult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.typemapper.AbstractTest;
import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;
import com.typemapper.namedresult.results.ClassWithGenderCodeEnum;
import com.typemapper.namedresult.results.GenderCodeEnum;

public class GenderCodeEnumTest extends AbstractTest {

    @Test
    public void testEnumMappings() throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("SELECT 'female' AS male_female");
        final ResultSet rs = ps.executeQuery();
        final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithGenderCodeEnum.class);
        int i = 0;
        while( rs.next() ) {
            ClassWithGenderCodeEnum result = (ClassWithGenderCodeEnum) mapper.mapRow(rs, i++);
            Assert.assertNotNull(result);
            Assert.assertEquals(GenderCodeEnum.FEMALE, result.getGender());
        }
    }
    
}
