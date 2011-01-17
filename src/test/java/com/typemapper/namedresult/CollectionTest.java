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
import com.typemapper.namedresult.results.ClassWithPrimitives;
import com.typemapper.namedresult.results.ClassWithSimpleCollection;

public class CollectionTest extends AbstractTest {
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		connection.prepareStatement("DROP SCHEMA IF EXISTS tmp CASCADE;").execute();
		connection.prepareStatement("CREATE SCHEMA tmp;").execute();
		connection.prepareStatement("CREATE TYPE tmp.simple_type AS (i int, l int, c varchar);").execute();
		//connection.prepareStatement("CREATE TYPE tmp.array_type AS (array tmp.simple_type[], str varchar);").execute();
	}
	
	@After
	public void tearDown() throws SQLException {
		connection.prepareStatement("DROP SCHEMA IF EXISTS tmp CASCADE;").execute();
	}
	
	@Test
	public void testSimpleList() throws Exception {
		final PreparedStatement ps = connection.prepareStatement("SELECT 'str' as str, ARRAY[ROW(1,2,'c')::tmp.simple_type, ROW(1,2,'c')::tmp.simple_type]::tmp.simple_type[] as arr");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ClassWithSimpleCollection.class);
		int i = 0;
		while( rs.next() ) {
			ClassWithSimpleCollection result = (ClassWithSimpleCollection) mapper.mapRow(rs, i++);
			Assert.assertNotNull(result);
			Assert.assertEquals("str", result.getStr());
			Assert.assertNotNull(result.getArray());
			Assert.assertTrue(result.getArray().size() == 2);
			Assert.assertNotNull(result.getArray().get(0));
			Assert.assertNotNull(result.getArray().get(1));
			ClassWithPrimitives classWithPrimitives = result.getArray().get(0);
			Assert.assertEquals(1, classWithPrimitives.getI());
			Assert.assertEquals(2, classWithPrimitives.getL());
			Assert.assertEquals('c', classWithPrimitives.getC());
			classWithPrimitives = result.getArray().get(1);
			Assert.assertEquals(1, classWithPrimitives.getI());
			Assert.assertEquals(2, classWithPrimitives.getL());
			Assert.assertEquals('c', classWithPrimitives.getC());

		}
	}
}
