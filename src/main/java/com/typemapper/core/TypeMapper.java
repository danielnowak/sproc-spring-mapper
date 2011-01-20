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
import com.typemapper.parser.postgres.ParseUtils;

@SuppressWarnings("rawtypes")
public class TypeMapper implements ParameterizedRowMapper {
	
	private static final Logger LOG = Logger.getLogger(TypeMapper.class);
	
	private final Class resultClass;
	private final List<Mapping> mappings;
	
	TypeMapper(Class resultClass) {
		this.resultClass = resultClass;
		mappings = Mapping.getMappingsForClass(this.resultClass);
	}

	@Override
	public Object mapRow(ResultSet set, int count) throws SQLException {
		Object result = null;
		try {
			
			result = getResultClass().newInstance();
			ResultTree resultTree = extractResultTree(set);
			fillObject(result, resultTree);
		} catch (InstantiationException e) {
			throw new SQLException(getResultClass() + " has not public no arch constructor", e);
		} catch (IllegalAccessException e) {
			throw new SQLException(getResultClass() + " has not public no arch constructor", e);
		}
		return result;
	}
	
	private ResultTree extractResultTree(ResultSet set) throws SQLException {
		ResultTree tree = new ResultTree();
		int i = 1;
		while (true) {
			String name = null;
			Object obj = null;
			DbResultNode node = null;
			try {
				obj = set.getObject(i);
				name = set.getMetaData().getColumnName(i);
			} catch (SQLException e) {
				LOG.error("Exception while extracting result Tree", e);
				break;
			}
			if (obj instanceof PGobject && ((PGobject)obj).getType().equals("record")) {
 				PGobject pgObj = (PGobject) obj;
				DbFunction function = DbFunctionRegister.getFunction(name, set.getStatement().getConnection());
				List<String> fieldValues = ParseUtils.getStringList(pgObj.getValue());
				int j = 1;
				for (String fieldValue : fieldValues) {
					DbTypeField fieldDef = function.getFieldByPos(j);
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
				PGobject pgObj = (PGobject) obj;
				node = new ObjectResultNode(pgObj.getValue(), name, pgObj.getType(), set.getStatement().getConnection());
			} else if (obj instanceof Jdbc4Array) {
				Jdbc4Array arrayObj = (Jdbc4Array) obj;
				String typeName = arrayObj.getBaseTypeName();
				String value = arrayObj.toString();
				node = new ArrayResultNode(name, value, typeName, set.getStatement().getConnection());
			} else {
				node = new SimpleResultNode(obj, name);
			}
			LOG.info("obj = " + obj);
			tree.addChild(node);
			i++;
		}
		return tree;
	}

	private void fillObject(Object result, ResultTree tree) throws SQLException {
		for (Mapping mapping :getMappings()) {
			try {
				DbResultNode node = tree.getChildByName(mapping.getName());
				if (DbResultNodeType.SIMPLE.equals(node.getNodeType())) {
					String fieldStringValue = node.getValue();
					Object value = mapping.getFieldMapper().mapField(fieldStringValue);
					mapping.map(result, value);
					
				} else if (DbResultNodeType.OBJECT.equals(node.getNodeType())) {
					Object value = ObjectFieldMapper.mapField(mapping.getFieldClass(), (ObjectResultNode) node);
					mapping.map(result, value);
					
				} else if (DbResultNodeType.ARRAY.equals(node.getNodeType())) {
					Object value = ArrayFieldMapper.mapField(mapping.getField(), (ArrayResultNode) node);
					mapping.map(result, value);
				}
			} catch (Exception e) {
				LOG.error(e, e);
			}
		}
	}

	public Class getResultClass() {
		return resultClass;
	}

	public List<Mapping> getMappings() {
		return mappings;
	}

}
