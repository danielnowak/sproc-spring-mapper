package com.typemapper.namedresult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

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
		connection.prepareStatement("CREATE SCHEMA tmp;").execute();
		connection.prepareStatement("CREATE TYPE tmp.simple_type AS (i int, l int, c varchar);").execute();
		connection.prepareStatement("CREATE TYPE tmp.simple_type_for_embed AS (i int, l int, c varchar, str varchar);").execute();
		connection.prepareStatement("CREATE TYPE tmp.complex_type AS (obj tmp.simple_type, str varchar);").execute();
		connection.prepareStatement("CREATE TYPE tmp.array_type AS (arr tmp.simple_type[], str varchar);").execute();
		/*
		
		connection.prepareStatement("DROP SCHEMA IF EXISTS tmp CASCADE;").execute();
		connection.prepareStatement("CREATE SCHEMA tmp;").execute();
		connection.prepareStatement("CREATE TYPE tmp.simple_type AS (i int, l int, c varchar);").execute();
		*/

	}	

}
