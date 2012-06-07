package com.typemapper.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DbTypeRegister {

    private static Map<String, DbTypeRegister> registers = null;

    private Map<String, DbType> types = null;
    private Map<String, List<String>> typeNameToFQN = null;
    private List<String> searchPath = null;

    public DbTypeRegister(final Connection connection) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            searchPath = getSearchPath(connection);
            typeNameToFQN = new HashMap<String, List<String>>();
            types = new HashMap<String, DbType>();
            statement = connection.prepareStatement(
                    "SELECT udt_schema, udt_name, attribute_name, ordinal_position, data_type, attribute_udt_name FROM information_schema.attributes");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int i = 1;
                final String typeSchema = resultSet.getString(i++);
                final String typeName = resultSet.getString(i++);
                final String fieldName = resultSet.getString(i++);
                final int fieldPosition = resultSet.getInt(i++);
                final String fieldType = resultSet.getString(i++);
                final String fieldTypeName = resultSet.getString(i++);
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

    public static List<String> getSearchPath(final Connection connection) throws SQLException {
        final ResultSet searchPathResult = connection.createStatement().executeQuery("show search_path;");
        searchPathResult.next();

        final String searchPathStr = searchPathResult.getString(1);
        return Arrays.asList(searchPathStr.split("\\s*,\\s*"));
    }

    private void addField(final String typeSchema, final String typeName, final String fieldName,
            final int fieldPosition, final String fieldType, final String fieldTypeName) {
        final String typeId = getTypeIdentifier(typeSchema, typeName);
        DbType type = types.get(typeId);
        if (type == null) {
            type = new DbType(typeSchema, typeName);
            addType(type);

        }

        type.addField(new DbTypeField(fieldName, fieldPosition, fieldType, fieldTypeName));
    }

    private void addType(final DbType type) {
        final String id = getTypeIdentifier(type.getSchema(), type.getName());
        types.put(id, type);

        List<String> list = typeNameToFQN.get(type.getName());
        if (list == null) {
            list = new LinkedList<String>();
            typeNameToFQN.put(type.getName(), list);
        }

        list.add(id);
    }

    private String getTypeIdentifier(final String typeSchema, final String typeName) {
        return typeSchema + "." + typeName;
    }

    public static DbType getDbType(final String name, final Connection connection) throws SQLException {
        final Map<String, DbTypeRegister> registry = initRegistry("default", connection);

        for (final DbTypeRegister register : registry.values()) {
            final List<String> list = register.typeNameToFQN.get(name);
            if (list != null) {
                final String fqName = SearchPathSchemaFilter.filter(list, register.searchPath);
                if (fqName != null) {
                    final DbType result = register.types.get(fqName);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }

        return null;
    }

    public static synchronized Map<String, DbTypeRegister> initRegistry(final String name, final Connection connection)
        throws SQLException {
        if (registers == null) {
            registers = new HashMap<String, DbTypeRegister>();
        }

        if (!registers.containsKey(name)) {
            registers.put(name, new DbTypeRegister(connection));
        }

        return registers;
    }

    public static void reInitRegister(final Connection connection) throws SQLException {
        if (registers == null) {
            registers = new HashMap<String, DbTypeRegister>();
        }

        registers.put("default", new DbTypeRegister(connection));
    }

}
