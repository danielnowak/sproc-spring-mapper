package com.typemapper.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class DbTypeRegister {
	
	private static DbTypeRegister register = null; 
	
	private Map<String, DbType> types = null;
	
	private static final Logger LOG = Logger.getLogger(DbTypeRegister.class);
	
	
	public DbTypeRegister(Connection connection) throws SQLException {
		
		this.types = new HashMap<String, DbType>();
		PreparedStatement statement = connection.prepareStatement("SELECT udt_schema, udt_name, attribute_name, ordinal_position, data_type, attribute_udt_name FROM information_schema.attributes");
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			int i = 1;
			String typeSchema = resultSet.getString(i++);
			String typeName = resultSet.getString(i++);
			String fieldName = resultSet.getString(i++);
			int fieldPosition = resultSet.getInt(i++);
			String fieldType = resultSet.getString(i++);
			String fieldTypeName = resultSet.getString(i++);
			addField(typeSchema, typeName, fieldName, fieldPosition, fieldType, fieldTypeName);
			
		}
	}
	
	private void addField(String typeSchema, String typeName, String fieldName, int fieldPosition, String fieldType, String fieldTypeName) {
		final String typeId = getTypeIdentifier(typeName);
		DbType type = types.get(typeId);
		if (type == null) {
			type = new DbType(typeSchema, typeName);
			types.put(typeId, type);
		}
		type.addField(new DbTypeField(fieldName, fieldPosition, fieldType, fieldTypeName));
	}

	private String getTypeIdentifier(String typeName) {
		return typeName;
	}

	public static DbType getDbType(String name, Connection connection) throws SQLException {
		if (register == null) {
			initRegister(connection);
		}
		String id = register.getTypeIdentifier(name);
		return register.types.get(id);
	}

	private static synchronized void initRegister(Connection connection) throws SQLException {
		if (register == null) {
			register = new DbTypeRegister(connection);
		}
		
	}

}
