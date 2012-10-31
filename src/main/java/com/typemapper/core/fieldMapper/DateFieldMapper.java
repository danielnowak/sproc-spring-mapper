package com.typemapper.core.fieldMapper;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import org.postgresql.jdbc2.PostgresJDBCDriverReusedTimestampUtils;

public class DateFieldMapper implements FieldMapper {

    private static final Logger LOG = Logger.getLogger(DateFieldMapper.class);
    private static PostgresJDBCDriverReusedTimestampUtils postgresJDBCDriverReusedTimestampUtils =
        new PostgresJDBCDriverReusedTimestampUtils();

    @Override
    public Object mapField(final String string, final Class<?> clazz) {

        if (string == null) {
            return null;
        }

        Timestamp date = null;
        try {
            date = postgresJDBCDriverReusedTimestampUtils.toTimestamp(null, string);
        } catch (final SQLException e) {
            LOG.error("Invalid date/time string: " + string, e);
        }

        if (date == null) {
            LOG.error("Could not parse date: " + string);
            return null;
        }

        if (clazz != null && clazz.equals(Date.class)) {
            return new Date(date.getTime());
        }

        return date;
    }
}
