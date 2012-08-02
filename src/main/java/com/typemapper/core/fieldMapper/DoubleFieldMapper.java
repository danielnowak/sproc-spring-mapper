package com.typemapper.core.fieldMapper;

import org.apache.log4j.Logger;

public class DoubleFieldMapper implements FieldMapper {

    private static final Logger LOG = Logger.getLogger(DoubleFieldMapper.class);

    @Override
    public Object mapField(final String string, final Class clazz) {
        try {
            return string == null ? null : Double.parseDouble(string);
        } catch (NumberFormatException e) {
            LOG.error("Could not convert " + string + " to double.", e);
        }

        return null;
    }
}
