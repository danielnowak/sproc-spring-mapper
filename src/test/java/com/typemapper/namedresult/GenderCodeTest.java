package com.typemapper.namedresult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import com.typemapper.AbstractTest;
import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;
import com.typemapper.namedresult.results.ClassWithGenderCode;
import com.typemapper.namedresult.results.GenderCode;

public class GenderCodeTest extends AbstractTest {
    
    @Test
    public void enumTransformation() throws Exception {
        final PreparedStatement stmt = connection.prepareStatement("SELECT 'femme' AS male_female;");
        final ResultSet result = stmt.executeQuery();
        final TypeMapper<ClassWithGenderCode> mapper = TypeMapperFactory.createTypeMapper(ClassWithGenderCode.class);
        int i = 0;
        while(result.next()) {
            final ClassWithGenderCode classWithGenderCode = mapper.mapRow(result, i++);
            assertNotNull(classWithGenderCode);
            assertEquals(GenderCode.FEMALE, classWithGenderCode.getGenderCode());
        }
    }

}
