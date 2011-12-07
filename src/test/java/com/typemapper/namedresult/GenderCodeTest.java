package com.typemapper.namedresult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.typemapper.AbstractTest;
import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;
import com.typemapper.namedresult.results.ClassWithGenderCode;
import com.typemapper.namedresult.results.GenderCode;

@RunWith(Parameterized.class)
public class GenderCodeTest extends AbstractTest {
    
    private final String dbCode;
    private final GenderCode genderCode;
    
    public GenderCodeTest(final String dbCode, final GenderCode genderCode) {
        this.dbCode = dbCode;
        this.genderCode = genderCode;
    }
    
    @Parameters
    public static Collection<Object[]> getParameters() {
        final Object[][] data = new Object[][] {
                {"femme", GenderCode.FEMALE},
                {"homme", GenderCode.MALE}
        };
        return Arrays.asList(data);
    }
    
    @Test
    public void enumTransformation() throws Exception {
        final PreparedStatement stmt = connection.prepareStatement(String.format("SELECT '%s' AS male_female;",
                dbCode));
        final ResultSet result = stmt.executeQuery();
        final TypeMapper<ClassWithGenderCode> mapper = TypeMapperFactory.createTypeMapper(ClassWithGenderCode.class);
        int i = 0;
        while(result.next()) {
            final ClassWithGenderCode classWithGenderCode = mapper.mapRow(result, i++);
            assertNotNull(classWithGenderCode);
            assertEquals(genderCode, classWithGenderCode.getGenderCode());
        }
    }

}
