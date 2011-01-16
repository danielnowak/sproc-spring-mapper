package com.typemapper.namedresult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;
import com.typemapper.namedresult.results.ClassWithObject;


public class ObjectTest extends AbstractTest {
	
	/*
DROP SCHEMA IF EXISTS tmp CASCADE;
CREATE SCHEMA tmp;
CREATE TYPE tmp.simple_type AS (i int, l int, c varchar);

CREATE TYPE tmp.complex_type AS (simple tmp.simple_type, c varchar);

CREATE TYPE tmp.array_type AS (simple tmp.simple_type[], c varchar);

	 */
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		connection.prepareStatement("DROP SCHEMA IF EXISTS tmp CASCADE;").execute();
		connection.prepareStatement("CREATE SCHEMA tmp;").execute();
		connection.prepareStatement("CREATE TYPE tmp.simple_type AS (i int, l int, c varchar);").execute();
	}
	
	@After
	public void tearDown() throws SQLException {
		connection.prepareStatement("DROP SCHEMA IF EXISTS tmp CASCADE;").execute();
	}
	
	

	@Test
	public void testPrimitiveMappings() throws SQLException {
		final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, ROW(1,2,'c')::tmp.simple_type as obj");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithObject.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithObject result = (ClassWithObject) mapper.mapRow(rs, i++);
			Assert.assertNotNull(result.getPrimitives());
			Assert.assertEquals(1, result.getPrimitives().getI());
			Assert.assertEquals(2, result.getPrimitives().getL());
			Assert.assertEquals('c', result.getPrimitives().getC());
			Assert.assertEquals("str", result.getStr());
		}
	}
	
	@Test
	public void testPrimitiveMappingsWithEmbeds() throws SQLException {
		final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, ROW(1,2,'c')::tmp.simple_type as obj");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithObject.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithObject result = (ClassWithObject) mapper.mapRow(rs, i++);
			Assert.assertNotNull(result.getPrimitives());
			Assert.assertEquals(1, result.getPrimitives().getI());
			Assert.assertEquals(2, result.getPrimitives().getL());
			Assert.assertEquals('c', result.getPrimitives().getC());
			Assert.assertEquals("str", result.getStr());
		}
	}
	

}
