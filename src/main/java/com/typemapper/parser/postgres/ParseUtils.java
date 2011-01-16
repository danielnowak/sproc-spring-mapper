package com.typemapper.parser.postgres;

import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ParseUtils {
	

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(ParseUtils.class);
    private static final String REGEX = "[\\(\\),]*";
    private static final String REGEX2 = "\\(,.*";
    private static final Pattern PATTERN_REGEX = Pattern.compile(REGEX);
    private static final Pattern PATTERN_REGEX2 = Pattern.compile(REGEX2);
    private static final DateTimeFormatter DATE_TIME_WITH_TIME_ZONE_FORMATTER = DateTimeFormat
            .forPattern("yyyy-MM-dd HH:mm:ssZ");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final int DEFAULT_STRING_BUILDER_SIZE = 128;
    private static final int LAST_DOUBLEPOINT_POSITION_FOR_TIMEZONE = 3;
    private static final int SIZE_OF_TWO_ESCAPES = 4;

    private ParseUtils() {
    }

    /**
     * If the string s is null, we simply return an emtyString.
     * 
     * @param s
     *            the string to trim and escape
     * 
     * 
     * @return string with white spaces removed at both ends and double quotes escaped (
     *         <code>"</code> => <code>\\"</code>)
     */
    public static String trimAndEscapeDoubleQuotes(final String s) {
        if (s == null) {
            return "";
        }
        return quotePgArrayElement(quotePgRowElement(s.trim()));
    }

    public static String quotePgRowElement(final String s) {
        if (s == null) {
            return "";
        }
        if ((s.indexOf('\\') == -1) && (s.indexOf('"') == -1)) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length() + SIZE_OF_TWO_ESCAPES);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\') {
                sb.append('\\').append('\\');
            } else if (c == '"') {
                sb.append('"').append('"');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String quotePgArrayElement(final String s) {
        if (s == null) {
            return "";
        }
        if ((s.indexOf('\\') == -1) && (s.indexOf('"') == -1)) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length() + SIZE_OF_TWO_ESCAPES);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\') {
                sb.append('\\').append('\\');
            } else if (c == '"') {
                sb.append('\\').append('"');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Appends a string quoted ready for CSV file to the given target StringBuilder
     * @param target target StringBulder instance (if null, a new StringBuilder is created)
     * @param value string to quote
     * @return target StringBuilder
     */
    public static StringBuilder quoteCsvValue(final StringBuilder target, final String value) {
        return quoteCsvValue(target, value, false);
    }

    /**
     * Appends a string quoted ready for CSV file to the given target StringBuilder
     * @param target target StringBulder instance (if null, a new StringBuilder is created)
     * @param value string to quote
     * @param prependWithComma if true, prepends the quoted value with a comma
     * (to be used when iterating through strings to be quoted)
     * @return target StringBuilder
     */
    public static StringBuilder quoteCsvValue(StringBuilder target, final String value,
            final boolean prependWithComma) {

        if (target == null) {
            target = new StringBuilder();
        }
        if (prependWithComma) {
            target.append(',');
        }
        if (value == null) {
            return target;
        }
        final int z = value.length();
        if (z == 0) {
            return target.append('"').append('"');
        } // quote empty string
        if ((value.indexOf('"') == -1) && (value.indexOf(',') == -1) && (value.indexOf('\n') == -1)) {
            return target.append(value);
        }
        target.append('"');
        for (int i = 0; i < z; i++) {
            char c = value.charAt(i);
            if (c == '"') {
                target.append('"').append('"');
            } else {
                target.append(c);
            }
        }
        target.append('"');
        return target;
    }

    /**
     * Appends a string quoted ready for CSV file to the given character Writer
     * @param dst destination character Writer instance (should be not null)
     * @param value string to quote
     */
    public static void quoteCsvValue(final Writer dst, final String value) throws IOException {
        quoteCsvValue(dst, value, false);
    }

    /**
     * Appends a string quoted ready for CSV file to the given character Writer
     * @param dst destination character Writer instance (should be not null)
     * @param value string to quote
     * @param prependWithComma if true, prepends the quoted value with a comma
     * (to be used when iterating through strings to be quoted)
     */
    public static void quoteCsvValue(final Writer dst, final String value, final boolean prependWithComma)
            throws IOException {

        if (prependWithComma) {
            dst.append(',');
        }
        if (value == null) {
            return;
        }
        final int z = value.length();
        if (z == 0) {
            dst.append('"').append('"'); // quote empty string
            return;
        }
        if ((value.indexOf('"') == -1) && (value.indexOf(',') == -1) && (value.indexOf('\n') == -1)) {
            dst.append(value);
            return;
        }
        dst.append('"');
        for (int i = 0; i < z; i++) {
            char c = value.charAt(i);
            if (c == '"') {
                dst.append('"').append('"');
            } else {
                dst.append(c);
            }
        }
        dst.append('"');
    }

    /**
     * Quotes a string to be ready for CSV file
     * @param value string to quote
     * @return quoted string
     */
    public static String quoteCsvValue(final String value) {
        if (value == null) {
            return "";
        }
        final int z = value.length();
        if (z == 0) {
            return "\"\"";
        } // quote empty string
        int quoteCount = 0;
        int commaCount = 0;
        int newLineCount = 0;
        for (int i = 0; i < z; i++) {
            char c = value.charAt(i);
            if (c == '"') {
                quoteCount++;
            } else if (c == ',') {
                commaCount++;
            } if ( c == '\n' ) {
                newLineCount++;
            }
        }
        if ((quoteCount == 0) && (commaCount == 0) && (newLineCount == 0)) {
            return value;
        }
        StringBuilder target = new StringBuilder(z + quoteCount + 2);
        target.append('"');
        for (int i = 0; i < z; i++) {
            char c = value.charAt(i);
            if (c == '"') {
                target.append('"').append('"');
            } else {
                target.append(c);
            }
        }
        target.append('"');
        return target.toString();
    }

    /**
     * parses {@link org.postgresql.util.PGobject} value into a {@link List} of {@link String}.
     * <p>
     * The value of a {@link org.postgresql.util.PGobject} is represented by a {@link String} and
     * contains of one or more columns, separated by comma. The row must begin with an open bracket
     * and must end with a closing bracket.
     * <p>
     * 
     * @param value
     *            , the {@link org.postgresql.util.PGobject} value
     * @return {@link List} of {@link String}
     * @throws RuntimeException
     */
    public static List<String> postgresROW2StringList(final String value) {
        return postgresROW2StringList(value, DEFAULT_STRING_BUILDER_SIZE);
    }

    /**
     * parses {@link org.postgresql.util.PGobject} value into a {@link List} of {@link String}.
     * <p>
     * The value of a {@link org.postgresql.util.PGobject} is represented by a {@link String} and
     * contains of one or more columns, separated by comma. The row must begin with an open bracket
     * and must end with a closing bracket.
     * <p>
     * The appendStringSize is the Size for StringBuilder.
     * 
     * @param value
     *            , the {@link org.postgresql.util.PGobject} value
     * @param size
     *            of the {@link StringBuilder}
     * @return {@link List} of {@link String}
     * @throws RuntimeException
     */
    public static List<String> postgresROW2StringList(final String value, final int size) {
        if (!(value.startsWith("(") && value.endsWith(")"))) {
            throw new RuntimeException(
                    "postgresROW2StringList() ROW has to start with '(' and ends with ')': " + value);
        }

        final List<String> result = new ArrayList<String>();

        if (PATTERN_REGEX.matcher(value).matches()) {
            for (int i = 1; i < value.length(); i++) {
                result.add("");
            }
            return result;
        }

        if (PATTERN_REGEX2.matcher(value).matches()) {
            result.add("");
        }

        final char[] c = value.toCharArray();
        StringBuilder element = new StringBuilder(size);
        int i = 1;
        while (c[i] != ')') {
            if (c[i] == ',') {
                if (c[i + 1] == ',') {
                    result.add("");
                } else if (c[i + 1] == ')') {
                    result.add("");
                }
                i++;
            } else if (c[i] == '\"') {
                i++;
                boolean insideQuote = true;
                while (insideQuote) {
                    final char nextChar = c[i + 1];
                    if (c[i] == '\"') {
                        if (nextChar == ',' || nextChar == ')') {
                            result.add(element.toString());
                            element = new StringBuilder(size);
                            insideQuote = false;
                        } else if (nextChar == '\"') {
                            i++;
                            element.append(c[i]);
                        } else {
                            throw new RuntimeException(
                                    "postgresROW2StringList() char after \" is not valid");
                        }
                    } else if (c[i] == '\\') {
                        if (nextChar == '\\' || nextChar == '\"') {
                            i++;
                            element.append(c[i]);
                        } else {
                            throw new RuntimeException(
                                    "postgresROW2StringList() char after \\ is not valid");
                        }
                    } else {
                        element.append(c[i]);
                    }
                    i++;
                }
            } else {
                while (!(c[i] == ',' || c[i] == ')')) {
                    element.append(c[i]);
                    i++;
                }
                result.add(element.toString());
                element = new StringBuilder(size);
            }
        }
        return result;
    }

    /**
     * parse a given postgres Timestamp as string to DateTime
     *
     * @param date
     *            as {@link String}
     * @return {@link DateTime}
     */
    public static DateTime getJodaDateTime(final String value) {
        if (value == null || value.length() == 0) {
            return null;
        }

        try {
            final String parseValue = value.split("\\.")[0];
            return DATE_TIME_FORMATTER.parseDateTime(parseValue);
        } catch (final Exception e) {
            LOG.error("An error occurred while parsing datetime value '" + value
                    + "'. Message: " + e.getMessage() + " - return null", e);
        }
        return null;
    }

    /**
     * parse a given postgres Timestamp as string to Date
     * 
     * @param date
     *            as {@link String}
     * @return {@link Date}
     */
    public static Date getDateTime(final String value) {
        final DateTime returnDate = getJodaDateTime(value);
        if (returnDate != null) {
            return returnDate.toDate();
        }
        return null;
    }

    /**
     * parse a given postgres Timestamp with or without timezone as string to Date.
     * If you are not shure what the database will return
     * then use this method.
     *
     * @param date as {@link String}
     *
     * @return {@link Date} or <code>null</code> if value could not be parsed
     */
    public static Date getDateTimeWithOrWithoutTimezone(final String value) {
        return getJodaDateTimeWithOrWithoutTimezone(value).toDate();
    }

    /**
     * parse a given postgres Timestamp with or without timezone as string to joda DateTime.
     * If you are not shure what the database will return
     * then use this method.
     * 
     * @param date as {@link String}
     * 
     * @return {@link DateTime} or <code>null</code> if value could not be parsed
     */
    public static DateTime getJodaDateTimeWithOrWithoutTimezone(final String value) {
        if (value == null) {
            return null;
        }

        // if datetime ends with :ss or :s (ss = seconds, s = second) then it does not have a timezone
        //   e.g. 2010-01-15 21:10:56 or 2010-01-15 21:10:5
        // if datetime has a . after the last : then it has milliseconds and does not have a timzone
        //   e.g. 2010-01-15 21:10:56.031723
        // otherwise it has a timezone
        //   e.g. 2010-01-18 00:00:00+01
        final int lastDoublePoint = value.lastIndexOf(':');
        final int lastPoint = value.lastIndexOf('.');

        if (lastDoublePoint >= value.length() - LAST_DOUBLEPOINT_POSITION_FOR_TIMEZONE || lastPoint > lastDoublePoint) {
            return getJodaDateTime(value);
        } else {
            return getJodaDateTimeWithTimezone(value);
        }
    }

    /**
     * parse a given postgres Timestamp with timezone as string to DateTime
     *
     * @param date as {@link String}
     * @return {@link DateTime}
     */
    public static DateTime getJodaDateTimeWithTimezone(final String value) {
        try {
            if (value.length() > 0) {
                final String parseValue = (value + "00").split("\\.")[0];
                return DATE_TIME_WITH_TIME_ZONE_FORMATTER.parseDateTime(parseValue);
            }
        } catch (final Exception e) {
            LOG.error("An error occurred while parsing datetime with timezone value '" + value
                    + "'. Message: " + e.getMessage() + " - return null", e);
        }
        return null;
    }

    /**
     * Returns a string with the given DateTime
     * @param value
     * @return
     */
    public static String getTimestampWithoutTimezone(final DateTime value) {
        return value.toString(DATE_TIME_FORMATTER);
    }

    /**
     * parse a given postgres Timestamp with timezone as string to Date
     * 
     * @param date as {@link String}
     * @return {@link Date}
     */
    public static Date getDateTimeWithTimezone(final String value) {
        final DateTime returnDate = getJodaDateTimeWithTimezone(value);
        if (returnDate != null) {
            return returnDate.toDate();
        }
        return null;
    }

    /**
     * parse a given postgres Date as String to Date
     * 
     * @param date
     *            as {@link String}
     * @return {@link Date}
     */
    public static Date getDate(final String value) {
        try {
            if (value.length() > 0) {
                return DATE_FORMATTER.parseDateTime(value).toDate();
            }
        } catch (final Exception e) {
            LOG.error("An error occurred while parsing date value '" + value + "'. Message: "
                    + e.getMessage() + " - return null", e);
        }
        return null;
    }

    /**
     * parse a given string to short
     */
    public static Short getShort(final String value) {
        try {
            if (value.length() > 0) {
                return Short.parseShort(value);
            }
        } catch (final NumberFormatException e) {
            LOG.error(e.getMessage() + " return null");
        }
        return null;
    }

    /**
     * parse a given postgres date as string
     * 
     * @param value
     * @return
     */
    public static Long getLong(final String value) {
        try {
            if (value.length() > 0) {
                return Long.parseLong(value);
            }
        } catch (final NumberFormatException e) {
            LOG.error(e.getMessage() + " return null");
            return null;
        }
        return null;
    }

    /**
     * parse a given String to Integer
     * 
     * @param value
     * @return
     */
    public static Integer getInteger(final String value) {
        try {
            if (value.length() > 0) {
                return Integer.parseInt(value);
            }
        } catch (final NumberFormatException e) {
            LOG.error(e.getMessage() + " return null");
            return null;
        }
        return null;
    }

    public static List<Integer> getIntegerList(final String value) {
        final String myValue = prepareGetNumberArray(value);

        if (myValue == null) {
            return null;
        }

        try {
            final List<String> sList = postgresROW2StringList(myValue);
            final List<Integer> iList = new ArrayList<Integer>();

            for (final Object o : sList.toArray()) {
                final Integer i = getInteger(o.toString());

                if (i != null) {
                    iList.add(i);
                }
            }
            return iList;
        } catch (final NumberFormatException e) {
            LOG.error(e.getMessage() + " return null");
            return null;
        }
    }

    public static Integer[] getIntegerArray(final String value) {
        return getIntegerList(value).toArray(new Integer[0]);
    }

    private static String prepareGetNumberArray(final String value) {
        if (value == null) {
            return null;
        }

        String myValue = null;
        if (value.length() > 0) {
            if (value.indexOf("=") > -1) {
                myValue = value.split("\\=")[1].replace("{", "(").replace("}", ")");
            } else {
                myValue = value.replace("{", "(").replace("}", ")");
            }
        } else {
            return null;
        }

        return myValue;
    }

    /**
     * parse a given String to Long[]
     * 
     * @param value
     * @return
     */
    public static Long[] getLongArray(final String value) {
        String myValue = null;
        myValue = prepareGetNumberArray(value);

        if (myValue == null) {
            return null;
        }

        try {
            final List<String> sList = postgresROW2StringList(myValue);
            final List<Long> iList = new ArrayList<Long>();

            for (final Object o : sList.toArray()) {
                final Long l = getLong(o.toString());

                if (l != null) {
                    iList.add(l);
                }
            }
            return iList.toArray(new Long[0]);
        } catch (final NumberFormatException e) {
            LOG.error(e.getMessage() + " return null");
            return null;
        }
    }

    /**
     * parse a given String to String[]
     * 
     * @param value
     * @return
     */
    public static List<String> getStringList(final String value) {
        if (value == null) {
            return null;
        }

        String myValue = null;
        if (value.length() > 0) {
            if (value.startsWith("{") && value.endsWith("}")) {
                if (value.length() == 2) {
                    // special case for empty postgres array ("{}")
                    return new ArrayList<String>(0);
                }
                myValue = "(" + value.substring(1, value.length() - 1) + ")";
            } else {
                myValue = value;
            }
        } else {
            return null;
        }
        return postgresROW2StringList(myValue);
    }

    /**
     * parse a given String -> to Map<Long, Long><br/>
     * e.g. [0:1]={"(1,5)","(2,6)"}, {"(1,5)","(2,6)"}
     * 
     * @param input
     * @return
     */
    public static Map<Long, Long> getLongMap(final String input) {
        String myInput = null;
        if (input.length() > 0) {
            if (input.indexOf("=") > -1) {
                myInput = input.split("\\=")[1].replaceAll(" ", "").replace("{", "(").replace("}",
                        ")");
            } else {
                myInput = input.replaceAll(" ", "").replace("{", "(").replace("}", ")");
            }
        } else {
            return null;
        }

        try {
            final List<String> l = postgresROW2StringList(myInput);
            final Map<Long, Long> map = new LinkedHashMap<Long, Long>();
            for (final String s : l) {
                final char[] c = s.toCharArray();
                Long key = null;
                Long value = null;
                for (int i = 0; i < c.length; i++) {
                    if (c[i] == '(') {
                        key = Long.valueOf(String.valueOf(c[i + 1]));
                    }
                    if (c[i] == ')') {
                        value = Long.valueOf(String.valueOf(c[i - 1]));
                    }
                    map.put(key, value);
                }
            }
            return map;
        } catch (final NumberFormatException e) {
            LOG.error(e.getMessage() + " return null");
            return null;
        }
    }

    /**
     * parse a given String to String
     * 
     * @param value
     * @return
     */
    public static String getString(final String value) {
        if (value != null && value.length() > 0) {
            return value;
        }
        return null;
    }

    /**
     * parse an Integer to String
     * @param value
     * @return
     */
    public static String getString(final Integer value) {
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }

    /**
     * parse a BigInteger to String
     * @param value
     * @return
     */
    public static String getString(final BigInteger value) {
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }

    public static boolean getBoolean(final String value) {
        return (value != null && value.equals("t"));
    }

    /**
     * parse a given String to Double
     * 
     * @param value
     * @return
     */
    public static Double getDouble(final String value) {
        try {
            if (value.length() > 0) {
                return Double.valueOf(value);
            }
        } catch (final NumberFormatException e) {
            LOG.error(e.getMessage() + " return null");
            return null;
        }
        return null;
    }

    /**
     * @param value
     * @return
     */
    public static Locale getLocale(final String value) {
        if (value.length() > 0) {
            final Scanner s = new Scanner(value);
            s.useDelimiter("_");

            final String language = s.next();
            final String country = s.next();
            final Locale l = new Locale(language, country);
            return l;
        }
        return Locale.GERMANY;
    }

    public static List<String> getArrayElements(final String serializedArray) {

        final List<String> elements = new ArrayList<String>();

        int currentPositionInString = 0;

        while (currentPositionInString != -1) {
            currentPositionInString = fetchElement(currentPositionInString, serializedArray,
                    elements);
        }

        return elements;
    }

    private static int fetchElement(final int fromIndex, final String serializedArray, final List<String> elements) {

        int n = serializedArray.indexOf('(', fromIndex) + 1;

        // we did not find something...
        if (n == 0) {
            return -1;
        }

        int depth = 0;

        final StringBuilder elementBuffer = new StringBuilder().append('(');

        while (n < serializedArray.length()) {

            final char currentChar = serializedArray.charAt(n);

            elementBuffer.append(currentChar);

            if (currentChar == '(') {
                depth++;
            }
            if (currentChar == ')' && depth == 0) {
                break;
            }
            if (currentChar == ')' && depth != 0) {
                depth--;
            }

            n++;
        }

        elements.add(elementBuffer.toString());

        return n;
    }

    /**
     * @param int2Parse
     * 
     * @return set of bit map values
     */
    public static Set<Integer> getBitSet(final int int2Parse) {
        final Set<Integer> set = new HashSet<Integer>();

        for (int value = 1; value <= int2Parse; value *= 2) {
            if ((int2Parse & value) == value) {
                set.add(value);
            }
        }

        return set;
    }	

}
