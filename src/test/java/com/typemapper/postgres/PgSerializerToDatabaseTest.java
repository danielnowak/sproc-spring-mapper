package com.typemapper.postgres;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

import static com.typemapper.postgres.PgArray.ARRAY;
import static com.typemapper.postgres.PgRow.ROW;

import java.sql.SQLException;
import java.sql.Types;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.typemapper.AbstractTest;

import com.typemapper.namedresult.results.ClassWithEnum;
import com.typemapper.namedresult.results.ClassWithPrimitives;
import com.typemapper.namedresult.results.ClassWithPrimitivesAndMap;
import com.typemapper.namedresult.results.Enumeration;

@RunWith(Parameterized.class)
public class PgSerializerToDatabaseTest extends AbstractTest {

    @Before
    public void createJdbcTemplate() {
        // BasicConfigurator.configure();
        // Logger.getRootLogger().setLevel(Level.INFO);
        // Logger.getLogger(org.springframework.jdbc.datasource.DataSourceUtils.class).setLevel(Level.INFO);
        // Logger.getLogger("org.springframework.jdbc.core.JdbcTemplate").setLevel(Level.WARN);
        // Logger.getLogger("org.springframework.beans").setLevel(Level.WARN);
        // Logger.getLogger("org.springframework.jdbc.support").setLevel(Level.WARN);

        this.template = new JdbcTemplate(new SingleConnectionDataSource(this.connection, false));
    }

    // Fields
    private JdbcTemplate template;
    private final Object objectToSerialize;
    private final String expectedString;
    private final int expectedSQLType;

    /*
     * Constructor. The JUnit test runner will instantiate this class once for
     * every element in the Collection returned by the method annotated with
     *
     * @Parameters.
     */
    public PgSerializerToDatabaseTest(final Object objectToSerialize, final String expectedString,
            final Integer expectedSQLType) {
        this.objectToSerialize = objectToSerialize;
        this.expectedString = expectedString;
        this.expectedSQLType = expectedSQLType;
    }

    private static Map<String, String> createSimpleMap(final String key, final String val) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put(key, val);
        return map;
    }

    /*
     * Test data generator. This method is called the the JUnit parameterized
     * test runner and returns a Collection of Arrays. For each Array in the
     * Collection, each array element corresponds to a parameter in the
     * constructor.
     */
    @Parameters
    public static Collection<Object[]> generateData() throws SQLException {
        return Arrays.asList(
                new Object[][] {

                    /* 0 */
                    {1, "1", Types.INTEGER},

                    /* 1 */
                    {Integer.valueOf(69), "69", Types.INTEGER},

                    /* 2 */
                    {true, "true", Types.BOOLEAN},

                    /* 3 */
                    {ARRAY(1, 2, 3, 4).asJdbcArray("int4"), "{1,2,3,4}", Types.ARRAY},

                    /* 4 */
                    {ARRAY(null, 2, 3, 4).asJdbcArray("int4"), "{NULL,2,3,4}", Types.ARRAY},

                    /* 5 */
                    {ARRAY("a", "b").asJdbcArray("text"), "{a,b}", Types.ARRAY},

                    /* 6 */
                    {
                        ARRAY("first element", "second \"quoted\" element").asJdbcArray("text"),
                        "{\"first element\",\"second \\\"quoted\\\" element\"}", Types.ARRAY
                    },

                    /* 7 */
                    {ROW(1, 2).asPGobject("int_duplet"), "(1,2)", Types.OTHER},

                    /* 8 */
                    {
                        ROW(1, 2, ARRAY("a", "b")).asPGobject("int_duplet_with_text_array"), "(1,2,\"{a,b}\")",
                        Types.OTHER
                    },

                    /* 9 */
                    {
                        ROW("a", "b", new int[] {1, 2, 3, 4}).asPGobject("text_duplet_with_int_array"),
                        "(a,b,\"{1,2,3,4}\")", Types.OTHER
                    },

                    /* 10 */
                    {
                        ROW("a", null, ARRAY(ROW(1, 10), ROW(2, 20), null)).asPGobject(
                            "text_duplet_with_int_duplet_array"), "(a,,\"{\"\"(1,10)\"\",\"\"(2,20)\"\",NULL}\")",
                        Types.OTHER
                    },

                    /* 11 */
                    {
                        ROW("a", null, ARRAY(ROW(1, 11), ROW(2, 22), null)).asPGobject(
                            "text_duplet_with_int_duplet_array"), "(a,,\"{\"\"(1,11)\"\",\"\"(2,22)\"\",NULL}\")",
                        Types.OTHER
                    },

                    /* 12 */
                    {
                        ROW(1, new ClassWithPrimitives(1, 2L, 'c')).asPGobject("int_with_additional_type"),
                        "(1,\"(c,1,2)\")", Types.OTHER
                    },

                    /* 13 */
                    {
                        ROW(1,
                            new ClassWithPrimitives[] {
                                new ClassWithPrimitives(1, 100L, 'a'), new ClassWithPrimitives(2, 200L, 'b')
                            }).asPGobject("int_with_additional_type_array"),
                        "(1,\"{\"\"(a,1,100)\"\",\"\"(b,2,200)\"\"}\")", Types.OTHER
                    },

                    /* 14 */
                    {PgTypeHelper.asPGobject(new ClassWithPrimitives(1, 100L, 'a')), "(a,1,100)", Types.OTHER},

                    /* 15 */
                    {
                        PgTypeHelper.asPGobject(new ClassWithPrimitivesAndMap(1, 2, 'a', createSimpleMap("b", "c"))),
                        "(1,2,a,\"\"\"b\"\"=>\"\"c\"\"\")", Types.OTHER
                    },

                    /* 16 */
                    {
                        PgTypeHelper.asPGobject(new ClassWithEnum(Enumeration.VALUE_1, Enumeration.VALUE_2)),
                        "(VALUE_1,VALUE_2)", Types.OTHER
                    },
                });
    }

    @Before
    public void createNeededTypes() throws SQLException {
        execute("CREATE TYPE tmp.int_duplet AS (a int, b int);");
        execute("CREATE TYPE tmp.int_duplet_with_text_array AS (a int, b int, c text[]);");
        execute("CREATE TYPE tmp.text_duplet_with_int_array AS (a text, b text, c int[]);");
        execute("CREATE TYPE tmp.text_duplet_with_int_duplet_array AS (a text, b text, c tmp.int_duplet[]);");

        // NOTE: additional_type members must be sorted alphabetically (because annotation positions were not defined)
        execute("CREATE TYPE tmp.additional_type AS (c text, i integer, l bigint);");
        execute("CREATE TYPE tmp.int_with_additional_type AS (a int, t tmp.additional_type);");
        execute("CREATE TYPE tmp.int_with_additional_type_array AS (a int, t tmp.additional_type[]);");

        // type with positional members:
        execute("CREATE TYPE tmp.additional_type_with_positions AS (i int, l bigint, c text, h hstore);");
    }

    @After
    public void dropUsedTypes() throws SQLException {
        execute("DROP TYPE tmp.text_duplet_with_int_duplet_array;");
        execute("DROP TYPE tmp.text_duplet_with_int_array;");
        execute("DROP TYPE tmp.int_duplet_with_text_array;");
        execute("DROP TYPE tmp.int_duplet;");
        execute("DROP TYPE tmp.int_with_additional_type;");
        execute("DROP TYPE tmp.int_with_additional_type_array;");
        execute("DROP TYPE tmp.additional_type;");
    }

    @Test
    public void passingParametersToQueryTest() {
        assertThat(template.queryForObject("SELECT (?)::text", new Object[] {this.objectToSerialize},
                new int[] {this.expectedSQLType}, String.class), is(this.expectedString));
    }
}
