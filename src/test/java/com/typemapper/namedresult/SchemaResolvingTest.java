package com.typemapper.namedresult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.typemapper.AbstractTest;
import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;
import com.typemapper.core.db.DbTypeRegister;
import com.typemapper.namedresult.results.ClassWithObject2;
import com.typemapper.namedresult.results.ClassWithPrimitives2;

public class SchemaResolvingTest extends AbstractTest {
	
	@Test
	public void testPrimitiveMappings() throws SQLException {
		connection.createStatement().execute("set search_path to tmp2,tmp");
		DbTypeRegister.reInitRegister(connection);
		final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, ROW(1,2,'c','h')::tmp2.simple_type as obj");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithObject2.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithObject2 result = (ClassWithObject2) mapper.mapRow(rs, i++);
			Assert.assertEquals(1, result.getPrimitives().getI());
			Assert.assertEquals(2, result.getPrimitives().getL());
			Assert.assertEquals('c', result.getPrimitives().getC());
			Assert.assertEquals('h', result.getPrimitives().getH());
			Assert.assertEquals("str", result.getStr());
		}
	}	

}
