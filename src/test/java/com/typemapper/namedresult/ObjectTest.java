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
import com.typemapper.namedresult.results.ClassWithObjectWithEmbed;
import com.typemapper.namedresult.results.ClassWithObjectWithObject;


public class ObjectTest extends AbstractTest {
	
	/*
DROP SCHEMA IF EXISTS tmp CASCADE;
CREATE SCHEMA tmp;
CREATE TYPE tmp.simple_type AS (i int, l int, c varchar);
CREATE TYPE tmp.complex_type AS (simple tmp.simple_type, c varchar);
CREATE TYPE tmp.array_type AS (simple tmp.simple_type[], c varchar);
CREATE TYPE tmp.simple_type_for_embed AS (i int, l int, c varchar, str varchar);

	 */
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@After
	public void tearDown() throws SQLException {
		//connection.prepareStatement("DROP SCHEMA IF EXISTS tmp CASCADE;").execute();
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
		//SELECT 1 as i, 2 as l, 'c' as c, 'str' as str
		final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, ROW(1,2,'c','str')::tmp.simple_type_for_embed as obj");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithObjectWithEmbed.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithObjectWithEmbed result = (ClassWithObjectWithEmbed) mapper.mapRow(rs, i++);
			Assert.assertEquals("str", result.getStr());
			Assert.assertNotNull(result.getClassWithEmbed());
			Assert.assertEquals("str", result.getClassWithEmbed().getStr());
			Assert.assertEquals(1, result.getClassWithEmbed().getPrimitives().getI());
			Assert.assertEquals(2, result.getClassWithEmbed().getPrimitives().getL());
			Assert.assertEquals('c', result.getClassWithEmbed().getPrimitives().getC());
		}
	}
	
	@Test
	public void testObjectInObject() throws SQLException {
		final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, ROW(ROW(1,2,'c')::tmp.simple_type,'str')::tmp.complex_type as obj");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithObjectWithObject.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithObjectWithObject result = (ClassWithObjectWithObject) mapper.mapRow(rs, i++);
			Assert.assertEquals("str", result.getStr());
			Assert.assertNotNull(result.getWithObj());
			Assert.assertEquals("str", result.getWithObj().getStr());
			Assert.assertNotNull("str", result.getWithObj().getPrimitives());
			Assert.assertEquals('c', result.getWithObj().getPrimitives().getC());
			Assert.assertEquals(2, result.getWithObj().getPrimitives().getL());
			Assert.assertEquals(1, result.getWithObj().getPrimitives().getI());
		}
		
	}
	

}
