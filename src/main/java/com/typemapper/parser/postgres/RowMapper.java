package com.typemapper.parser.postgres;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

public class RowMapper {
	
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(RowMapper.class);

    /**
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public final Element mapRow(final ResultSet rs, final String columnName) throws SQLException {
        final Element element = new Element();
        final List<String> l = ParseUtils.postgresROW2StringList(rs.getString(columnName));
        element.setRowList(l);
        return element;
    }

    /**
     * @param rs
     * @param columnNameArray
     * @return
     * @throws SQLException
     */
    public final List<Element> mapRowToList(final ResultSet rs, final String columnNameArray) throws SQLException {
        if (rs == null) {
            return Collections.emptyList();
        }

        final Array sqlArray = rs.getArray(columnNameArray);

        if (sqlArray == null) {
            return Collections.emptyList();
        } else {
            final ResultSet resultSet = sqlArray.getResultSet();
            final List<String> l = new ArrayList<String>();
            while (resultSet.next()) {
                String test = resultSet.getString(2);
                l.add(test);
            }

            List<Element> elements = new ArrayList<Element>(l.size());
            for (String s : l) {
                try {
                    List<String> resultList = ParseUtils.postgresROW2StringList(s);
                    Element element = new Element();
                    element.setRowList(resultList);
                    elements.add(element);
                } catch (Exception e) {
                    LOG.error("Problem parsing received ROW value [" + s + "]: " + e.getMessage(), e);
                }
            }
            return elements;
        }

    }	

}
