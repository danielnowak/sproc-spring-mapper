package com.typemapper.serialization.postgres;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.event.ListSelectionEvent;

public class SerializationUtils {

    public static final class SerializationError extends Exception {
        private static final long serialVersionUID = -8967467321268976338L;
        public SerializationError(String errorMessage) {
            super(errorMessage);
        }
    }
    
    private static final String NULL = "NULL";
    private static final char DOUBLE_QUOTE = '"';
    private static final char BACKSLASH = '\\';
    private static final char COMMA = ',';

    private static abstract class PgCollectionSerializer<E> {

        final protected Collection<E> collection;
        
        protected PgCollectionSerializer(Collection<E> c) {
            this.collection = c;
        }
        
        final public boolean isNull() {
            return collection == null;
        }
        
        protected abstract char getOpeningChar();
        protected abstract char getClosingChar();
        
        final protected String getEmpty() {
            return new StringBuilder(2).append(getOpeningChar()).append(getClosingChar()).toString();
        }
        
        protected abstract void quoteChar(StringBuilder sb, char ch);
        
        protected abstract void appendNull(StringBuilder sb);

        final public StringBuilder quote(StringBuilder sb, CharSequence s) {
            if ( sb == null ) throw new NullPointerException("Passed StringBuilder should be not null");
            if ( s == null ) throw new NullPointerException("Null values should be processed by the caller");
            final int l = s.length();
            if ( l == 0 ) return sb.append("\"\"");
            // find if there are quotes or commas in the string
            boolean needsQuotation = false;
            int neededLength = 0;
            for( int i = 0; i < l; i++ ) {
                final char ch = s.charAt(i);
                neededLength++;
                if ( ch == DOUBLE_QUOTE || ch == BACKSLASH ) {
                    needsQuotation = true;
                    neededLength++;
                }
                if ( Character.isWhitespace(ch) || ch == COMMA || ch == getOpeningChar() || ch == getClosingChar() ) {
                    needsQuotation = true;
                }
            }
            if ( needsQuotation ) neededLength += 2;
            sb.ensureCapacity(sb.length() + neededLength);
            // start quotation
            if ( needsQuotation ) sb.append(DOUBLE_QUOTE);
            for( int i = 0; i < l; i++ ) {
                final char ch = s.charAt(i);
                if ( ch == DOUBLE_QUOTE || ch == BACKSLASH ) {
                    quoteChar(sb, ch);
                } else {
                    sb.append(ch);
                }
            }
            if ( needsQuotation ) sb.append(DOUBLE_QUOTE);
            return sb;
        }
        
        final public String toPgString() throws SerializationError {
            if ( collection == null ) throw new NullPointerException("Null value of Array depends on the context, should use isNull() call before");
            if(collection.isEmpty()) {
                return getEmpty();
            }
            final Iterator<E> iterator = collection.iterator();
            StringBuilder sb = new StringBuilder();
            sb.append(getOpeningChar());
            boolean hasNext = iterator.hasNext();
            while( hasNext ) {
                E element = iterator.next();
                if ( element == null ) {
                    appendNull(sb);
                } else {
                    final Class<?> clazz = element.getClass();
                    if ( element instanceof PgCollectionSerializer ) {
                        quote(sb, ((PgCollectionSerializer<?>)element).toPgString() );
                    } else if ( clazz.isArray() ) {
                        final Class<?> componentClazz = clazz.getComponentType();
                        if ( componentClazz.isPrimitive() ) {
                            // we are fucked up again with the primitive arrays
                            // cast it into a string array
                            final int l = Array.getLength(element);
                            final String[] stringArray = new String[l];
                            for (int i = 0; i < l; i++) {
                                stringArray[i] = String.valueOf(Array.get(element, i));
                            }
                            quote(sb, PgArray.ARRAY((Object[])stringArray).toPgString());
                        } else {
                            quote(sb, PgArray.ARRAY((Object[])element).toPgString());
                        }
                    } else if ( element instanceof Collection ) {
                        quote(sb, PgArray.ARRAY((Collection<?>)element).toPgString());
                    } else if ( clazz == Boolean.TYPE || clazz == Boolean.class ) {
                        sb.append( ( (Boolean) element) ? 't' : 'f' );
                    } else if ( clazz.isPrimitive() ||
                                element instanceof Number
                              ) {
                        sb.append(element);
                    } else if ( element instanceof CharSequence ||
                                element instanceof Character ||
                                clazz == Character.TYPE
                              ) {
                        quote(sb, String.valueOf(element));
                    } else {
                        throw new SerializationError("Cannot serialize type " + clazz.getName());
                    }
                    // TODO: Add support for ROW and HStore
                }
                hasNext = iterator.hasNext();
                if ( hasNext ) {
                    sb.append(',');
                } else {
                    break;
                }
            }
            sb.append(getClosingChar());
            return sb.toString();
        }
    }
    
    public final static class PgRow extends PgCollectionSerializer<Object> {
        
        protected PgRow(Collection<Object> c) {
            super(c);
        }

        public static final PgRow ROW(Object... array) {
            return PgRow.ROW(array == null ? null : Arrays.asList(array));
        }
        
        @SuppressWarnings("unchecked")
        public static final PgRow ROW(Collection<?> collection) {
            return new PgRow( (Collection<Object>) collection );
        }
        
        @Override
        final protected char getOpeningChar() {
            return '(';
        }

        @Override
        final protected char getClosingChar() {
            return ')';
        }

        @Override
        final protected void quoteChar(StringBuilder sb, char ch) {
            sb.append(ch).append(ch);
        }

        @Override
        final protected void appendNull(StringBuilder sb) {
            // do nothing as NULL is serialized as nothing to ROW
        }

    }
    
    public final static class PgArray<E> extends PgCollectionSerializer<E> {

        protected PgArray(Collection<E> c) {
            super(c);
        }

        public static final <T> PgArray<T> ARRAY(T... array) {
            return PgArray.ARRAY(array == null ? null : Arrays.asList(array));
        }

        public static final <T> PgArray<T> ARRAY(Collection<T> collection) {
            return new PgArray<T>(collection);
        }
        
        @Override
        final protected char getOpeningChar() {
            return '{';
        }

        @Override
        final protected char getClosingChar() {
            return '}';
        }

        @Override
        final protected void quoteChar(StringBuilder sb, char ch) {
            sb.append(BACKSLASH).append(ch);
        }

        @Override
        final protected void appendNull(StringBuilder sb) {
            sb.append(NULL);
        }
    }

    public static String toPgString(final Object o) throws SerializationError {
        if (o == null) return "NULL";
        final Class<?> clazz = o.getClass();
        if ( o instanceof PgCollectionSerializer ) {
            return ((PgCollectionSerializer<?>)o).toPgString();
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
                return PgArray.ARRAY((Object[])stringArray).toPgString();
            } else {
                return PgArray.ARRAY((Object[])o).toPgString();
            }
        } else if ( o instanceof Collection ) {
            return PgArray.ARRAY((Collection<?>)o).toPgString();
        } else if ( clazz == Boolean.TYPE || clazz == Boolean.class ) {
            return ((Boolean) o) ? "t" : "f";
        } else if ( clazz.isPrimitive() ||
                    o instanceof Number
                  ) {
            return String.valueOf(o);
        } else if ( o instanceof CharSequence ||
                    o instanceof Character ) {
            return o.toString();
        } else if ( clazz == Character.TYPE ) {
            return String.valueOf(o);
        } else {
            throw new SerializationError("Cannot serialize type " + clazz.getName());
        }
    }

}
