package com.typemapper.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.postgresql.jdbc4.Jdbc4Array;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.typemapper.core.fieldMapper.ArrayFieldMapper;
import com.typemapper.core.fieldMapper.ObjectFieldMapper;
import com.typemapper.core.result.ArrayResultNode;
import com.typemapper.core.result.DbResultNode;
import com.typemapper.core.result.DbResultNodeType;
import com.typemapper.core.result.ObjectResultNode;
import com.typemapper.core.result.ResultTree;
import com.typemapper.core.result.SimpleResultNode;

public class TypeMapper<ITEM> implements ParameterizedRowMapper<ITEM> {
	
	private static final Logger LOG = Logger.getLogger(TypeMapper.class);
	
	private final Class<ITEM> resultClass;
	private final List<Mapping> mappings;
	
	TypeMapper(Class<ITEM> resultClass) {
		this.resultClass = resultClass;
		mappings = Mapping.getMappingsForClass(this.resultClass);
	}

	@Override
	public ITEM mapRow(ResultSet set, int count) throws SQLException {
		ITEM result = null;
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
				obj = set.getArray(i);
				obj = set.getObject(i);
				name = set.getMetaData().getColumnName(i);
			} catch (SQLException e) {
				LOG.error("Exception while extracting result Tree", e);
				break;
			}
			if (obj instanceof PGobject) {
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

	public Class<ITEM> getResultClass() {
		return resultClass;
	}

	public List<Mapping> getMappings() {
		return mappings;
	}

}
