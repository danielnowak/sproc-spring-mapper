package com.typemapper.serialization.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class PgArray<E> extends AbstractPgCollectionSerializer<E> {

    protected PgArray(Collection<E> c) {
        super(c);
    }

    public static final <T> PgArray<T> ARRAY(T... array) {
        return PgArray.ARRAY(array == null ? null : Arrays.asList(array));
    }

    public static final <T> PgArray<T> ARRAY(Collection<T> collection) {
        return new PgArray<T>(collection);
    }
    
    @Override
    final protected char getOpeningChar() {
        return '{';
    }

    @Override
    final protected char getClosingChar() {
        return '}';
    }

    @Override
    final protected void quoteChar(StringBuilder sb, char ch) {
        sb.append(BACKSLASH).append(ch);
    }

    @Override
    final protected void appendNull(StringBuilder sb) {
        sb.append(SerializationUtils.NULL);
    }

    public static class PgGenericJdbcArray implements java.sql.Array {

        final static private Map<String, Integer> _pgGenericTypeNameToSQLTypeMap;
        static {
            HashMap<String, Integer> m = new HashMap<String, Integer>();
            m.put("int2", Types.SMALLINT);
            m.put("smallint", Types.SMALLINT);
            m.put("int4", Types.INTEGER);
            m.put("integer", Types.INTEGER);
            m.put("int", Types.INTEGER);
            m.put("oid", Types.BIGINT);
            m.put("int8", Types.BIGINT);
            m.put("bigint", Types.BIGINT);
            m.put("money", Types.DOUBLE);
            m.put("numeric", Types.NUMERIC);
            m.put("decimal", Types.NUMERIC);
            m.put("float4", Types.REAL);
            m.put("float8", Types.DOUBLE);
            m.put("float", Types.DOUBLE);
            m.put("char", Types.CHAR);
            m.put("bpchar", Types.CHAR);
            m.put("character", Types.CHAR);
            m.put("varchar", Types.VARCHAR);
            m.put("character verrying", Types.VARCHAR);
            m.put("text", Types.VARCHAR);
            m.put("name", Types.VARCHAR);
            m.put("bytea", Types.BINARY);
            m.put("bool", Types.BOOLEAN);
            m.put("boolean", Types.BOOLEAN);
            m.put("bit", Types.BIT);
            m.put("date", Types.DATE);
            m.put("time", Types.TIME);
            m.put("timetz", Types.TIME);
            m.put("timestamp", Types.TIMESTAMP);
            m.put("timestamptz", Types.TIMESTAMP);
            _pgGenericTypeNameToSQLTypeMap = Collections.unmodifiableMap(m);
        }

        final static public int getSQLType(final String typeName) {
            final Integer n = _pgGenericTypeNameToSQLTypeMap.get(typeName.trim().toLowerCase(Locale.US));
            if (n == null) {
                return Types.OTHER;
            } else {
                return n;
            }
        }

        private final String elementTypeName;
        private final PgArray<?> array;

        public <E> PgGenericJdbcArray(final String elementTypeName, PgArray<E> array) {
            this.elementTypeName = elementTypeName;
            this.array = (PgArray<?>) array;
        }

        @Override
        public String toString() {
            return array.toString();
        }
        
        @Override
        public void free() throws SQLException {
            throw new SQLFeatureNotSupportedException("Feature not supported");
        }

        @Override
        public Object getArray() throws SQLException {
            return array.collection.toArray();
        }

        @Override
        public Object getArray(Map<String, Class<?>> map) throws SQLException {
            throw new SQLFeatureNotSupportedException("Feature not supported");
        }

        @Override
        public Object getArray(long index, int count) throws SQLException {
            throw new SQLFeatureNotSupportedException("Feature not supported");
        }

        @Override
        public Object getArray(long index, int count, Map<String, Class<?>> map) throws SQLException {
            throw new SQLFeatureNotSupportedException("Feature not supported");
        }

        @Override
        public int getBaseType() throws SQLException {
            return getSQLType(elementTypeName);
        }

        @Override
        public String getBaseTypeName() throws SQLException {
            return elementTypeName;
        }

        @Override
        public ResultSet getResultSet() throws SQLException {
            throw new SQLFeatureNotSupportedException("Feature not supported");
        }

        @Override
        public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException {
            throw new SQLFeatureNotSupportedException("Feature not supported");
        }

        @Override
        public ResultSet getResultSet(long index, int count) throws SQLException {
            throw new SQLFeatureNotSupportedException("Feature not supported");
        }

        @Override
        public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws SQLException {
            throw new SQLFeatureNotSupportedException("Feature not supported");
        }
    }
    
    final public java.sql.Array asJdbcArray(final String elementTypeName) {
        return new PgGenericJdbcArray(elementTypeName, this);
    }

}