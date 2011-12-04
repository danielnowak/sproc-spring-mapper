package com.typemapper.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.postgresql.jdbc4.Jdbc4Array;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.typemapper.core.db.DbFunction;
import com.typemapper.core.db.DbFunctionRegister;
import com.typemapper.core.db.DbTypeField;
import com.typemapper.core.fieldMapper.ArrayFieldMapper;
import com.typemapper.core.fieldMapper.ObjectFieldMapper;
import com.typemapper.core.result.ArrayResultNode;
import com.typemapper.core.result.DbResultNode;
import com.typemapper.core.result.DbResultNodeType;
import com.typemapper.core.result.ObjectResultNode;
import com.typemapper.core.result.ResultTree;
import com.typemapper.core.result.SimpleResultNode;
import com.typemapper.parser.exception.RowParserException;
import com.typemapper.parser.postgres.ParseUtils;

public class TypeMapper<ITEM> implements ParameterizedRowMapper<ITEM> {
	
	private static final Logger LOG = Logger.getLogger(TypeMapper.class);
	
	private final Class<ITEM> resultClass;
	private final List<Mapping> mappings;
	
	TypeMapper(final Class<ITEM> resultClass) {
		this.resultClass = resultClass;
		mappings = Mapping.getMappingsForClass(this.resultClass);
		@SuppressWarnings("rawtypes")
		Class parentClass = this.resultClass.getSuperclass();
		while (parentClass != null) {
			mappings.addAll(Mapping.getMappingsForClass(parentClass));
			parentClass = parentClass.getSuperclass();
		}
	}

	@Override
	public ITEM mapRow(final ResultSet set, final int count) throws SQLException {
		ITEM result = null;
		try {
			
			result = getResultClass().newInstance();
			final ResultTree resultTree = extractResultTree(set);
			fillObject(result, resultTree);
		} catch (final InstantiationException e) {
			throw new SQLException(getResultClass() + " has not public no arch constructor", e);
		} catch (final IllegalAccessException e) {
			throw new SQLException(getResultClass() + " has not public no arch constructor", e);
		}
		return result;
	}
	
	private ResultTree extractResultTree(final ResultSet set) throws SQLException {
		LOG.debug("Extracting result tree");
		final ResultTree tree = new ResultTree();
		int i = 1;
		while (true) {
			String name = null;
			Object obj = null;
			DbResultNode node = null;
			try {
				obj = set.getObject(i);
				name = set.getMetaData().getColumnName(i);
			} catch (final SQLException e) {
				LOG.debug("End of result set reached");
				break;
			}
			if (obj instanceof PGobject && ((PGobject)obj).getType().equals("record")) {
 				final PGobject pgObj = (PGobject) obj;
				final DbFunction function = DbFunctionRegister.getFunction(name, set.getStatement().getConnection());
				List<String> fieldValues;
				try {
					fieldValues = ParseUtils.postgresROW2StringList(pgObj.getValue());
				} catch (final RowParserException e) {
					throw new SQLException(e);
				}

				int j = 1;
				for (final String fieldValue : fieldValues) {
					final DbTypeField fieldDef = function.getFieldByPos(j);
					DbResultNode currentNode = null;
					if (fieldDef.getType().equals("USER-DEFINED")) {
						currentNode = new ObjectResultNode(fieldValue, fieldDef.getName(), fieldDef.getTypeName(), set.getStatement().getConnection());
					} else if (fieldDef.getType().equals("ARRAY")) {
						currentNode = new ArrayResultNode(fieldDef.getName(), fieldValue, fieldDef.getTypeName().substring(1), set.getStatement().getConnection());
					} else {
						currentNode = new SimpleResultNode(fieldValue, fieldDef.getName());
					}
					tree.addChild(currentNode);
					j++;
				}
				i++;
				continue;
			} else if (obj instanceof PGobject) {
				final PGobject pgObj = (PGobject) obj;
				node = new ObjectResultNode(pgObj.getValue(), name, pgObj.getType(), set.getStatement().getConnection());
			} else if (obj instanceof Jdbc4Array) {
				final Jdbc4Array arrayObj = (Jdbc4Array) obj;
				final String typeName = arrayObj.getBaseTypeName();
				final String value = arrayObj.toString();
				node = new ArrayResultNode(name, value, typeName, set.getStatement().getConnection());
			} else {
				node = new SimpleResultNode(obj, name);
			}
			tree.addChild(node);
			i++;
		}
		LOG.info("Extracted ResultTree: " + tree);
		return tree;
	}

	private void fillObject(final Object result, final ResultTree tree) throws SQLException {
		for (final Mapping mapping : getMappings()) {
			try {
				final DbResultNode node = tree.getChildByName(mapping.getName());
				if (node == null) {
					LOG.error("Could not map property with name " + mapping.getName());
					continue;
				}
				if (DbResultNodeType.SIMPLE.equals(node.getNodeType())) {
					final String fieldStringValue = node.getValue();
					final Object value = mapping.getFieldMapper().mapField(fieldStringValue, mapping.getFieldClass());
					mapping.map(result, value);
					
				} else if (DbResultNodeType.OBJECT.equals(node.getNodeType())) {
					final Object value = ObjectFieldMapper.mapField(mapping.getFieldClass(), (ObjectResultNode) node);
					mapping.map(result, value);
					
				} else if (DbResultNodeType.ARRAY.equals(node.getNodeType())) {
					final Object value = ArrayFieldMapper.mapField(mapping.getField(), (ArrayResultNode) node);
					mapping.map(result, value);
				}
			} catch (final Exception e) {
				LOG.error(e, e);
			}
		}
	}

	public Class<ITEM> getResultClass() {
		return resultClass;
	}

	public List<Mapping> getMappings() {
		return mappings;
	}

}
