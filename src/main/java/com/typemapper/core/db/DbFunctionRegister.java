package com.typemapper.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DbFunctionRegister {
	
	private Map<String, DbFunction> functions = null;
	private static DbFunctionRegister register;
	
	
	public DbFunctionRegister(Connection connection) throws SQLException {
		
		this.functions = new HashMap<String, DbFunction>();
		PreparedStatement statement = connection.prepareStatement("SELECT specific_schema, specific_name,  parameter_name, ordinal_position, data_type, udt_name FROM information_schema.parameters WHERE parameter_mode='OUT';");
		ResultSet resultSet = statement.executeQuery();
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
	}

	private void addFunctionParam(String functionSchema, String functionName,
			String paramName, int paramPosition, String paramType,
			String paramTypeName) {
		final String functionId = getFunctionIdentifier(functionName);
		DbFunction function = functions.get(functionId);
		if (function == null) {
			function = new DbFunction(functionSchema, functionName);
			functions.put(functionId, function);
		}
		function.addOutParam(new DbTypeField(paramName, paramPosition, paramType, paramTypeName));
	}
	
	private static String getFunctionIdentifier(String functionName) {
		return functionName;
	}

	public static final DbFunction getFunction(String name, Connection connection) throws SQLException {
		if (register == null) {
			initRegistry(connection);
		}
		return register.functions.get(DbFunctionRegister.getFunctionIdentifier(name));
	}

	private static synchronized void initRegistry(Connection connection) throws SQLException {
		if (register == null) {
			register = new DbFunctionRegister(connection);
		}
	}

}
