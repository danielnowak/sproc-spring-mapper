package com.typemapper.core;

import java.sql.Connection;
import java.sql.SQLException;

import com.typemapper.core.db.DbFunctionRegister;
import com.typemapper.core.db.DbTypeRegister;

public class TypeMapperFactory {
	
	public static final <ITEM> TypeMapper<ITEM> createTypeMapper(Class<ITEM> clazz) {
		return new TypeMapper<ITEM>(clazz);
	}

	public static final void initTypeAndFunctionCaches(final Connection connection, final String name) throws SQLException {
		DbFunctionRegister.initRegistry(connection, name);
		DbTypeRegister.initRegistry(name, connection);
	}
}
