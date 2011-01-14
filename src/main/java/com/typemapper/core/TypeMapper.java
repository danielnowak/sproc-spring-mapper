package com.typemapper.core;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.typemapper.core.result.DbResultNode;
import com.typemapper.core.result.ResultTree;
import com.typemapper.core.result.SimpleResultNode;

public class TypeMapper implements ParameterizedRowMapper {
	
	private static final Logger LOG = Logger.getLogger(TypeMapper.class);
	
	@SuppressWarnings("unchecked")
	private final Class resultClass;
	private final List<Mapping> mappings;
	
	@SuppressWarnings("unchecked")
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
	
	private ResultTree extractResultTree(ResultSet set) {
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
			if (obj instanceof PGobject) {
				
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
				String fieldStringValue = node.getValue();
				Object value = mapping.getFieldMapper().mapField(fieldStringValue);
				mapping.map(result, value);
			} catch (Exception e) {
				LOG.error(e, e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Class getResultClass() {
		return resultClass;
	}

	public List<Mapping> getMappings() {
		return mappings;
	}

}
