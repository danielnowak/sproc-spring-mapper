package com.typemapper.namedresult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;
import com.typemapper.namedresult.results.ClassWithEmbed;
import com.typemapper.namedresult.results.ClassWithPrimitives;

public class NamedResultTest {
	
	private Connection connection;
	
	@Before
	public void setUp() throws Exception {
		// Get connection
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://localhost/postgres";
		Properties props = new Properties();
		props.setProperty("user","postgres");
		props.setProperty("password","postgres");
		connection = DriverManager.getConnection(url, props);
	}
	
	@Test
	public void testPrimitiveMappings() throws SQLException {
		final PreparedStatement ps = connection.prepareStatement("SELECT 1 as i, 2 as l, 'c' as c");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithPrimitives.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithPrimitives result = (ClassWithPrimitives) mapper.mapRow(rs, i++);
			Assert.assertNotNull(result);
			Assert.assertEquals(1, result.getI());
			Assert.assertEquals(2, result.getL());
			Assert.assertEquals('c', result.getC());
		}
	}
	
	@Test
	public void testPrimitiveMappingsWithEmbed() throws SQLException {
		final PreparedStatement ps = connection.prepareStatement("SELECT 1 as i, 2 as l, 'c' as c, 'str' as str");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithEmbed.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithEmbed result = (ClassWithEmbed) mapper.mapRow(rs, i++);
			Assert.assertNotNull(result.getPrimitives());
			Assert.assertEquals(1, result.getPrimitives().getI());
			Assert.assertEquals(2, result.getPrimitives().getL());
			Assert.assertEquals('c', result.getPrimitives().getC());
			Assert.assertEquals("str", result.getStr());
		}
	}	

}
