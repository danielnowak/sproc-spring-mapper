package com.typemapper.serialization.postgres;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

import org.postgresql.util.PGobject;

public final class PgRow extends AbstractPgCollectionSerializer<Object> {
    
    protected PgRow(Collection<Object> c) {
        super(c);
    }

    public static final PgRow ROW(Object... array) {
        return PgRow.ROW(array == null ? null : Arrays.asList(array));
    }
    
    @SuppressWarnings("unchecked")
    public static final PgRow ROW(Collection<?> collection) {
        return new PgRow( (Collection<Object>) collection );
    }
    
    @Override
    final protected char getOpeningChar() {
        return '(';
    }

    @Override
    final protected char getClosingChar() {
        return ')';
    }

    @Override
    final protected void quoteChar(StringBuilder sb, char ch) {
        sb.append(ch).append(ch);
    }

    @Override
    final protected void appendNull(StringBuilder sb) {
        // do nothing as NULL is serialized as nothing to ROW
    }

    private static final class PgGenericObject extends PGobject {

        private static final long serialVersionUID = -2855096142894174113L;

        public PgGenericObject(final String recordTypeName, final PgRow pgRow) throws SQLException {
            this.setType(recordTypeName);
            this.setValue(pgRow.toString());
        }
    }
    
    public PGobject asPGobject(final String recordTypeName) throws SQLException {
        return new PgGenericObject(recordTypeName, this);
    }
}