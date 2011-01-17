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
	}	

}
