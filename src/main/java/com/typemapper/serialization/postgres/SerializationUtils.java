package com.typemapper.serialization.postgres;

import java.lang.reflect.Array;
import java.util.Collection;

import org.postgresql.util.PGobject;

public class SerializationUtils {

    public static final class SerializationError extends Exception {
        private static final long serialVersionUID = -8967467321268976338L;
        public SerializationError(String errorMessage) {
            super(errorMessage);
        }
    }
    
    static final String NULL = "NULL";

    public static String toPgString(final Object o) throws SerializationError {
        if (o == null) return NULL;
        final Class<?> clazz = o.getClass();
        if ( o instanceof AbstractPgCollectionSerializer || 
             o instanceof PGobject ||
             o instanceof java.sql.Array ||
             o instanceof CharSequence ||
             o instanceof Character ||
             clazz == Character.TYPE )
        {
            return o.toString();
        } else if ( clazz.isArray() ) {
            final Class<?> componentClazz = clazz.getComponentType();
            if ( componentClazz.isPrimitive() ) {
                // we are fucked up again with the primitive arrays
                // cast it into a string array
                final int l = Array.getLength(o);
                final String[] stringArray = new String[l];
                for (int i = 0; i < l; i++) {
                    stringArray[i] = String.valueOf(Array.get(o, i));
                }
                return PgArray.ARRAY((Object[])stringArray).toString();
            } else {
                return PgArray.ARRAY((Object[])o).toString();
            }
        } else if ( o instanceof Collection ) {
            return PgArray.ARRAY((Collection<?>)o).toString();
        } else if ( clazz == Boolean.TYPE || clazz == Boolean.class ) {
            return ((Boolean) o) ? "t" : "f";
        } else if ( clazz.isPrimitive() ||
                    o instanceof Number
                  ) {
            return String.valueOf(o);
        } else {
            throw new SerializationError("Cannot serialize type " + clazz.getName());
        }
    }

}
