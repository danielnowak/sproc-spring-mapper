package com.typemapper.core;

import java.sql.Connection;
import java.sql.SQLException;

import com.typemapper.core.db.DbFunctionRegister;
import com.typemapper.core.db.DbTypeRegister;
import com.typemapper.core.fieldMapper.GlobalValueTransformerRegistry;

public class TypeMapperFactory {

    private TypeMapperFactory() {
        // private constructor: Factory cannot be instantiated
    }

    public static <ITEM> TypeMapper<ITEM> createTypeMapper(final Class<ITEM> clazz) {
        return new TypeMapper<ITEM>(clazz);
    }

    public static void initTypeAndFunctionCaches(final Connection connection, final String name) throws SQLException {
        DbFunctionRegister.initRegistry(connection, name);
        DbTypeRegister.initRegistry(name, connection);
    }

    public static void registerGlobalValueTransformer(final Class<?> clazz,
            final ValueTransformer<?, ?> valueTransformer) {
        GlobalValueTransformerRegistry.register(clazz, valueTransformer);
    }
}
