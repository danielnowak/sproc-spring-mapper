package com.typemapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class AbstractTest {
	
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
		connection.prepareStatement("DROP SCHEMA IF EXISTS tmp2 CASCADE;").execute();
		connection.prepareStatement("CREATE SCHEMA tmp;").execute();
		connection.prepareStatement("CREATE SCHEMA tmp2;").execute();
		connection.prepareStatement("set search_path to tmp,tmp2;").execute();
		connection.prepareStatement("CREATE TYPE tmp.simple_type AS (i int, l int, c varchar);").execute();
		connection.prepareStatement("CREATE TYPE tmp2.simple_type AS (i int, l int, c varchar, h varchar);").execute();
		connection.prepareStatement("CREATE TYPE tmp.simple_type_for_embed AS (i int, l int, c varchar, str varchar);").execute();
		connection.prepareStatement("CREATE TYPE tmp.complex_type AS (obj tmp.simple_type, str varchar);").execute();
		connection.prepareStatement("CREATE TYPE tmp.array_type AS (arr tmp.simple_type[], str varchar);").execute();
		connection.prepareStatement("CREATE TABLE tmp.simple_table (i int, l int, c varchar);").execute();
		connection.prepareStatement("INSERT INTO tmp.simple_table (i, l, c) VALUES (1,2,'Daniel'), (2,3,'alone at'), (3,4,'home');").execute();
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
		
		String primitive_sproc_2 = 	"CREATE OR REPLACE FUNCTION tmp2.primitives_function(OUT id smallint, OUT msg text, OUT msg2 text) " +
		"RETURNS record AS " +
		"$BODY$ " + 
		"DECLARE " +  
		"BEGIN " + 
			"id  := 0; " + 
			"msg := 'result_code'; " +
			"msg2 := 'result_code_2'; " +
			"RETURN; " +
		"END " + 
" $BODY$ " +
" LANGUAGE 'plpgsql' VOLATILE SECURITY DEFINER ";
connection.prepareStatement(primitive_sproc_2).execute();
		
		
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
		
		connection.prepareStatement("DROP SCHEMA IF EXISTS tmp CASCADE;").execute();
		connection.prepareStatement("CREATE SCHEMA tmp;").execute();
		connection.prepareStatement("CREATE TYPE tmp.simple_type AS (i int, l int, c varchar);").execute();
		*/

	}
	
	@After
	public void tearDown() throws SQLException {
		connection.prepareStatement("DROP SCHEMA IF EXISTS tmp CASCADE;").execute();
	}
	

}
