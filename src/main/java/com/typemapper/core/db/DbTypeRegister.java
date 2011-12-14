package com.typemapper.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DbTypeRegister {
	
	private static Map<String,DbTypeRegister> registers = null; 
	
	private Map<String, DbType> types = null;
	private Map<String, List<String>> typeNameToFQN = null;
	private String searchPath = null; 
	
	public DbTypeRegister(Connection connection) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			ResultSet searchPathResult = connection.createStatement().executeQuery("show search_path;");
			searchPathResult.next();
			searchPath = searchPathResult.getString(1);
			this.typeNameToFQN = new HashMap<String, List<String>>();
			this.types = new HashMap<String, DbType>();
			statement = connection.prepareStatement("SELECT udt_schema, udt_name, attribute_name, ordinal_position, data_type, attribute_udt_name FROM information_schema.attributes");
			resultSet = statement.executeQuery();
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
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
	}
	
	private void addField(String typeSchema, String typeName, String fieldName, int fieldPosition, String fieldType, String fieldTypeName) {
		final String typeId = getTypeIdentifier(typeSchema, typeName);
		DbType type = types.get(typeId);
		if (type == null) {
			type = new DbType(typeSchema, typeName);
			addType(type);
		
		}
		type.addField(new DbTypeField(fieldName, fieldPosition, fieldType, fieldTypeName));
	}

	private void addType(DbType type) {
		final String id = getTypeIdentifier(type.getSchema(), type.getName());
		types.put(id, type);
		List<String> list = typeNameToFQN.get(type.getName());
		if (list == null) {
			list = new LinkedList<String>();
			typeNameToFQN.put(type.getName(), list);
		}
		list.add(id);
	}

	private String getTypeIdentifier(final String typeSchema, String typeName) {
		return typeSchema + "." + typeName;
	}

	public static DbType getDbType(String name, Connection connection) throws SQLException {
		if (registers == null) {
			initRegistry("default", connection);
		}
		for (DbTypeRegister register : registers.values()) {
			List<String> list = register.typeNameToFQN.get(name);
			if (list != null) {
				if (list.size() == 1) {
					return register.types.get(list.get(0));
				} else {
					String fqName = SearchPathSchemaFilter.filter(list, register.searchPath);
					DbType result = register.types.get(fqName);
					if (result != null) {
						return result;
					}
				}
			} 
		}
		return null;
	}

	public static synchronized void initRegistry(final String name, Connection connection) throws SQLException {
		if (registers == null) {
			registers = new HashMap<String, DbTypeRegister>();
		}
		if (!registers.containsKey(name)) {
			registers.put(name, new DbTypeRegister(connection));
		}

	}
	
	public static void reInitRegister(Connection connection) throws SQLException {
		if (registers == null) {
			registers = new HashMap<String, DbTypeRegister>();
		}
		registers.put("default", new DbTypeRegister(connection));
	}

}
