package com.typemapper.postgres;

import java.sql.SQLException;

import java.util.Arrays;
import java.util.Collection;

import org.postgresql.util.PGobject;

public final class PgRow extends PGobject {

    private static final long serialVersionUID = -2855096142894174113L;

    private final PgRowSerializer rowSerializer;

    private final class PgRowSerializer extends AbstractPgCollectionSerializer<Object> {
        protected PgRowSerializer(final Collection<Object> c) {
            super(c);
        }

        @Override
        protected char getOpeningChar() {
            return '(';
        }

        @Override
        protected char getClosingChar() {
            return ')';
        }

        @Override
        protected void quoteChar(final StringBuilder sb, final char ch) {
            sb.append(ch).append(ch);
        }

        @Override
        protected void appendNull(final StringBuilder sb) {
            // do nothing as NULL is serialized as nothing for ROW
        }
    }

    protected PgRow(final String recordTypeName, final Collection<Object> c) throws SQLException {
        this.rowSerializer = new PgRowSerializer(c);
        this.setType(recordTypeName);
        this.setValue(rowSerializer.toString());
    }

    public static PgRow ROW(final Object... array) {
        return PgRow.ROW(null, array == null ? null : Arrays.asList(array));
    }

    @SuppressWarnings("unchecked")
    public static PgRow ROW(final Collection<?> collection) throws SQLException {
        return new PgRow(null, (Collection<Object>) collection);
    }

    public PGobject asPGobject(final String recordTypeName) throws SQLException {
        this.setType(recordTypeName);
        return this;
    }
}
