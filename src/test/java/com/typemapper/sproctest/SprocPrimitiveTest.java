package com.typemapper.sproctest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.typemapper.core.TypeMapper;
import com.typemapper.core.TypeMapperFactory;
import com.typemapper.namedresult.results.ClassWithObjectWithObject;
import com.typemapper.sproctest.result.ArrayResult;
import com.typemapper.sproctest.result.PrimitiveResult;

public class SprocPrimitiveTest {

	protected Connection connection;
	
	@Before
	public void setUp() throws Exception {
		// Get connection
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://localhost/postgres";
		Properties props = new Properties();
		props.setProperty("user","postgres");
		props.setProperty("password","postgres");
		connection = DriverManager.getConnection(url, props);
		connection.prepareStatement("DROP SCHEMA IF EXISTS tmp CASCADE;").execute();
		connection.prepareStatement("CREATE SCHEMA tmp;").execute();
		connection.prepareStatement("CREATE TABLE tmp.simple_table (i int, l int, c varchar);").execute();
		connection.prepareStatement("INSERT INTO tmp.simple_table (i, l, c) VALUES (1,2,'Daniel'), (2,3,'alone at'), (3,4,'home');").execute();
		connection.prepareStatement("CREATE TYPE tmp.simple_type AS (i int, l int, c varchar);").execute();
		String primitive_sproc = 	"CREATE OR REPLACE FUNCTION tmp.primitives_function(OUT id smallint, OUT msg text) " +
									"RETURNS record AS " +
									"$BODY$ " + 
									"DECLARE " +  
									"BEGIN " + 
										"id  := 0; " + 
										"msg := 'result_code'; " +
										"RETURN; " +
									"END " + 
" $BODY$ " +
" LANGUAGE 'plpgsql' VOLATILE SECURITY DEFINER ";
		connection.prepareStatement(primitive_sproc).execute();
		
		String array_sproc = 	"CREATE OR REPLACE FUNCTION tmp.array_function(OUT id smallint, OUT msg text, OUT movies tmp.simple_type[]) " +
		"RETURNS record AS " +
		"$BODY$ " + 
		"DECLARE " +  
		"BEGIN " + 
			"id  := 0; " + 
			"msg := 'result_code'; " +
			"movies := ARRAY(SELECT ROW(i,l,c)::tmp.simple_type FROM tmp.simple_table ORDER BY i); " +
			"RETURN; " +
		"END " + 
" $BODY$ " +
" LANGUAGE 'plpgsql' VOLATILE SECURITY DEFINER ";
		
		connection.prepareStatement(array_sproc).execute();
		/*
		connection.prepareStatement("CREATE TYPE tmp.simple_type_for_embed AS (i int, l int, c varchar, str varchar);").execute();
		connection.prepareStatement("CREATE TYPE tmp.complex_type AS (obj tmp.simple_type, str varchar);").execute();
		connection.prepareStatement("CREATE TYPE tmp.array_type AS (arr tmp.simple_type[], str varchar);").execute();
		
		
		connection.prepareStatement("DROP SCHEMA IF EXISTS tmp CASCADE;").execute();
		connection.prepareStatement("CREATE SCHEMA tmp;").execute();
		connection.prepareStatement("CREATE TYPE tmp.simple_type AS (i int, l int, c varchar);").execute();
		*/

	}	
	
	@After
	public void tearDown() throws SQLException {
		connection.prepareStatement("DROP SCHEMA IF EXISTS tmp CASCADE;").execute();
	}
	
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
