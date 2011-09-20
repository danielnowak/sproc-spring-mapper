package com.typemapper.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DbFunctionRegister {
	
	private Map<String, DbFunction> functions = null;
	private Map<String, List<String>> functionNameToFQName = null;
	private static DbFunctionRegister register;
	private String searchPath = null;
	
	
	public DbFunctionRegister(Connection connection) throws SQLException {
		PreparedStatement statement =  null;
		ResultSet resultSet = null;
		try {
			ResultSet searchPathResult = connection.createStatement().executeQuery("show search_path;");
			searchPathResult.next();
			searchPath = searchPathResult.getString(1);
			this.functionNameToFQName = new HashMap<String, List<String>>();
			this.functions = new HashMap<String, DbFunction>();
			statement = connection.prepareStatement("SELECT specific_schema, specific_name,  parameter_name, ordinal_position, data_type, udt_name FROM information_schema.parameters WHERE parameter_mode='OUT';");
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int i = 1;
				String functionSchema = resultSet.getString(i++);
				String functionName = resultSet.getString(i++);
				int sep = functionName.lastIndexOf('_');
				if (sep != -1) {
					functionName = functionName.substring(0, sep);
				}
				String paramName = resultSet.getString(i++);
				int paramPosition = resultSet.getInt(i++);
				String paramType = resultSet.getString(i++);
				String paramTypeName = resultSet.getString(i++);
				addFunctionParam(functionSchema, functionName, paramName, paramPosition, paramType, paramTypeName);
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

	private void addFunctionParam(String functionSchema, String functionName,
			String paramName, int paramPosition, String paramType,
			String paramTypeName) {
		final String functionId = getFunctionIdentifier(functionSchema, functionName);
		DbFunction function = functions.get(functionId);
		if (function == null) {
			function = new DbFunction(functionSchema, functionName);
			addFunction(function);
		}
		function.addOutParam(new DbTypeField(paramName, paramPosition, paramType, paramTypeName));
	}
	
	private void addFunction(DbFunction function) {
		String functionIdentifier = getFunctionIdentifier(function.getSchema(), function.getName());
		functions.put(functionIdentifier, function);
		List<String> list = functionNameToFQName.get(function.getName());
		if (list == null) {
			list = new LinkedList<String>();
			functionNameToFQName.put(function.getName(), list);
		}
		list.add(functionIdentifier);
	}

	private static String getFunctionIdentifier(String schema, String functionName) {
		return schema + "." + functionName;
	}

	public static final DbFunction getFunction(String name, Connection connection) throws SQLException {
		if (register == null) {
			initRegistry(connection);
		}
		List<String> list = register.functionNameToFQName.get(name);
		if (list.size() == 1) {
			return register.functions.get(list.get(0)); 
		} else {
			String fqName = SearchPathSchemaFilter.filter(list, register.searchPath);
			return register.functions.get(fqName);
		}
	}
	
	public static void reInitRegistry(Connection connection) throws SQLException {
		register = new DbFunctionRegister(connection);
	}

	private static synchronized void initRegistry(Connection connection) throws SQLException {
		if (register == null) {
			register = new DbFunctionRegister(connection);
		}
	}

}
