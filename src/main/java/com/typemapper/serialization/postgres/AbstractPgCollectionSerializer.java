package com.typemapper.serialization.postgres;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import org.postgresql.util.PGobject;


abstract class AbstractPgCollectionSerializer<E> {

    static final char DOUBLE_QUOTE = '"';
    static final char BACKSLASH = '\\';
    static final char COMMA = ',';
    
    final protected Collection<E> collection;
    
    protected AbstractPgCollectionSerializer(Collection<E> c) {
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
        final char openingChar = getOpeningChar();
        final char closingChar = getClosingChar();
        boolean needsQuotation = false;
        int neededLength = 0;
        for( int i = 0; i < l; i++ ) {
            final char ch = s.charAt(i);
            neededLength++;
            if ( ch == DOUBLE_QUOTE || ch == BACKSLASH ) {
                needsQuotation = true;
                neededLength++;
            }
            if ( Character.isWhitespace(ch) || ch == COMMA || ch == openingChar || ch == closingChar ) {
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
    
    @Override
    final public String toString() {
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
                if ( element instanceof AbstractPgCollectionSerializer ||
                     element instanceof PGobject ||
                     element instanceof java.sql.Array ||
                     element instanceof CharSequence ||
                     element instanceof Character ||
                     clazz == Character.TYPE) 
                {
                     quote(sb, element.toString() );
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
                        quote(sb, PgArray.ARRAY((Object[])stringArray).toString());
                    } else {
                        quote(sb, PgArray.ARRAY((Object[])element).toString());
                    }
                } else if ( element instanceof Collection ) {
                    quote(sb, PgArray.ARRAY((Collection<?>)element).toString());
                } else if ( clazz == Boolean.TYPE || clazz == Boolean.class ) {
                    sb.append( ( (Boolean) element) ? 't' : 'f' );
                } else if ( clazz.isPrimitive() ||
                            element instanceof Number
                          ) {
                    sb.append(element);
                } else {
                    throw new IllegalArgumentException("Cannot serialize type " + clazz.getName());
                }
                // TODO: Add support for HStore
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