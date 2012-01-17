package com.typemapper.postgres;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

import static com.typemapper.postgres.PgArray.ARRAY;
import static com.typemapper.postgres.PgRow.ROW;

import java.sql.SQLException;
import java.sql.Types;

import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.typemapper.AbstractTest;

import com.typemapper.namedresult.results.ClassWithPrimitives;

@RunWith(Parameterized.class)
public class PgSerializerToDatabaseTest extends AbstractTest {

    @Before
    public void createJdbcTemplate() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);
        Logger.getLogger(org.springframework.jdbc.datasource.DataSourceUtils.class).setLevel(Level.INFO);
        Logger.getLogger("org.springframework.jdbc.core.JdbcTemplate").setLevel(Level.WARN);
        Logger.getLogger("org.springframework.beans").setLevel(Level.WARN);
        Logger.getLogger("org.springframework.jdbc.support").setLevel(Level.WARN);

        this.template = new JdbcTemplate(new SingleConnectionDataSource(this.connection, false));
    }

    // Fields
    private JdbcTemplate template;
    private Object objectToSerialize;
    private String expectedString;
    private int expectedSQLType;

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
                        "(1,\"(1,2,c)\")", Types.OTHER
                    },

                    /* 13 */
                    {
                        ROW(1,
                            new ClassWithPrimitives[] {
                                new ClassWithPrimitives(1, 100L, 'a'), new ClassWithPrimitives(2, 200L, 'b')
                            }).asPGobject("int_with_additional_type_array"),
                        "(1,\"{\"\"(1,100,a)\"\",\"\"(2,200,b)\"\"}\")", Types.OTHER
                    },

                    /* 14 */
                    {PgTypeHelper.asPGobject(new ClassWithPrimitives(1, 100L, 'a')), "(1,100,a)", Types.OTHER}
                });
    }

    @Before
    public void createNeededTypes() throws SQLException {
        this.connection.prepareStatement("CREATE TYPE tmp.int_duplet AS (a int, b int);").execute();
        this.connection.prepareStatement("CREATE TYPE tmp.int_duplet_with_text_array AS (a int, b int, c text[]);")
                       .execute();
        this.connection.prepareStatement("CREATE TYPE tmp.text_duplet_with_int_array AS (a text, b text, c int[]);")
                       .execute();
        this.connection.prepareStatement(
            "CREATE TYPE tmp.text_duplet_with_int_duplet_array AS (a text, b text, c tmp.int_duplet[]);").execute();
        this.connection.prepareStatement("CREATE TYPE tmp.additional_type AS (i integer, l bigint, c text);").execute();
        this.connection.prepareStatement("CREATE TYPE tmp.int_with_additional_type AS (a int, t tmp.additional_type);")
                       .execute();
        this.connection.prepareStatement(
            "CREATE TYPE tmp.int_with_additional_type_array AS (a int, t tmp.additional_type[]);").execute();
    }

    @After
    public void dropUsedTypes() throws SQLException {
        this.connection.prepareStatement("DROP TYPE tmp.text_duplet_with_int_duplet_array;").execute();
        this.connection.prepareStatement("DROP TYPE tmp.text_duplet_with_int_array;").execute();
        this.connection.prepareStatement("DROP TYPE tmp.int_duplet_with_text_array;").execute();
        this.connection.prepareStatement("DROP TYPE tmp.int_duplet;").execute();
        this.connection.prepareStatement("DROP TYPE tmp.int_with_additional_type;").execute();
        this.connection.prepareStatement("DROP TYPE tmp.int_with_additional_type_array;").execute();
        this.connection.prepareStatement("DROP TYPE tmp.additional_type;").execute();
    }

    @Test
    public void passingParametersToQueryTest() {
        assertThat(template.queryForObject("SELECT (?)::text", new Object[] {this.objectToSerialize},
                new int[] {this.expectedSQLType}, String.class), is(this.expectedString));
    }
}
