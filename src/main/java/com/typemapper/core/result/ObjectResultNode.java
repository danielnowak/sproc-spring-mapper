package com.typemapper.core.result;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.typemapper.core.db.DbType;
import com.typemapper.core.db.DbTypeField;
import com.typemapper.core.db.DbTypeRegister;

import com.typemapper.parser.exception.RowParserException;
import com.typemapper.parser.postgres.ParseUtils;

public class ObjectResultNode implements DbResultNode {

    private static final Logger LOG = Logger.getLogger(ObjectResultNode.class);

    private String name;
    private String type;
    private List<DbResultNode> children;

    public ObjectResultNode(final String value, final String name, final String typeName, final Connection connection)
        throws SQLException {
        super();
        this.type = typeName;
        this.children = new ArrayList<DbResultNode>();
        this.name = name;

        List<String> values;
        if (value == null) {
            children = null;
            return;
        }

        try {
            values = ParseUtils.postgresROW2StringList(value);
        } catch (final RowParserException e) {
            throw new SQLException(e);
        }

        final DbType dbType = DbTypeRegister.getDbType(typeName, connection);
        int i = 1;
        for (final String fieldValue : values) {
            final DbTypeField fieldDef = dbType.getFieldByPos(i);
            DbResultNode node = null;
            if (fieldDef == null) {
                LOG.error("Could not find field in " + dbType + " for pos " + i);
                continue;
            }

            if (fieldDef.getType().equals("USER-DEFINED")) {
                if (fieldDef.getTypeName().equals("hstore")) {
                    node = new SimpleResultNode(fieldValue, fieldDef.getName());
                } else if (fieldDef.getTypeName().equals("enumeration")) {
                    node = new SimpleResultNode(fieldValue, fieldDef.getName());
                } else {
                    node = new ObjectResultNode(fieldValue, fieldDef.getName(), fieldDef.getTypeName(), connection);
                }
            } else if (fieldDef.getType().equals("ARRAY")) {
                node = new ArrayResultNode(fieldDef.getName(), fieldValue, fieldDef.getTypeName().substring(1),
                        connection);
            } else {
                node = new SimpleResultNode(fieldValue, fieldDef.getName());
            }

            this.children.add(node);
            i++;
        }
    }

    public String getType() {
        return type;
    }

    @Override
    public DbResultNodeType getNodeType() {
        return DbResultNodeType.OBJECT;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public List<DbResultNode> getChildren() {
        return children;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public DbResultNode getChildByName(final String name) {
        for (final DbResultNode node : getChildren()) {
            if (node.getName().equals(name)) {
                return node;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "ObjectResultNode [name=" + name + ", type=" + type + ", children=" + children + "]";
    }

}
