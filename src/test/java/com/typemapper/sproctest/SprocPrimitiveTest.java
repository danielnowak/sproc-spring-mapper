package com.typemapper.sproctest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.typemapper.AbstractTest;
import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;
import com.typemapper.sproctest.result.ArrayResult;
import com.typemapper.sproctest.result.PrimitiveResult;

public class SprocPrimitiveTest extends AbstractTest {


	
	@Test
	public void testPrimitives() throws SQLException {
		final PreparedStatement ps = connection.prepareStatement("SELECT tmp.primitives_function();");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(PrimitiveResult.class);
		int i = 0;
		while( rs.next() ) {
			PrimitiveResult result = (PrimitiveResult) mapper.mapRow(rs, i++);
			Assert.assertEquals(0, result.getId().intValue());
			Assert.assertEquals("result_code", result.getMsg());
		}
	}
	
	@Test
	public void testObjectArray() throws SQLException {
		final PreparedStatement ps = connection.prepareStatement("SELECT tmp.array_function();");
		final ResultSet rs = ps.executeQuery();
		final TypeMapper mapper = TypeMapperFactory.createTypeMapper(ArrayResult.class);
		int i = 0;
		while( rs.next() ) {
			ArrayResult result = (ArrayResult) mapper.mapRow(rs, i++);
			Assert.assertEquals(0, result.getId().intValue());
			Assert.assertEquals("result_code", result.getMsg());
			Assert.assertNotNull(result.getMovies());
			Assert.assertEquals(3, result.getMovies().size());
			Assert.assertEquals(1, result.getMovies().get(0).getI());
			Assert.assertEquals(2, result.getMovies().get(0).getL());
			Assert.assertEquals("Daniel", result.getMovies().get(0).getC());
			Assert.assertEquals(2, result.getMovies().get(1).getI());
			Assert.assertEquals(3, result.getMovies().get(1).getL());
			Assert.assertEquals("alone at", result.getMovies().get(1).getC());
			Assert.assertEquals(3, result.getMovies().get(2).getI());
			Assert.assertEquals(4, result.getMovies().get(2).getL());
			Assert.assertEquals("home", result.getMovies().get(2).getC());
			
			
		}
	}	
}
