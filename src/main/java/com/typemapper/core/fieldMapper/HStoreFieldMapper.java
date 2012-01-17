package com.typemapper.core.fieldMapper;

import org.apache.log4j.Logger;

import com.typemapper.parser.postgres.HStore;

public class HStoreFieldMapper implements FieldMapper {

    private static final Logger LOG = Logger.getLogger(HStoreFieldMapper.class);

    @Override
    public Object mapField(String value, Class clazz) {
        if (value == null) {
            return null;
        }

        HStore hstore = new HStore(value);
        return hstore.asMap();
    }

}
