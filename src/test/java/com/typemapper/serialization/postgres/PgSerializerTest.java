package com.typemapper.serialization.postgres;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.typemapper.AbstractTest;
import com.typemapper.serialization.postgres.SerializationUtils.SerializationError;

import static com.typemapper.serialization.postgres.PgRow.ROW;
import static com.typemapper.serialization.postgres.PgArray.ARRAY;

@RunWith(Parameterized.class)
public class PgSerializerTest extends AbstractTest {

    @Before
    public void createJdbcTemplate() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);
        Logger.getLogger(org.springframework.jdbc.datasource.DataSourceUtils.class).setLevel(Level.INFO);
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
    public PgSerializerTest(Object objectToSerialize, String expectedString, Integer expectedSQLType) {
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
        return Arrays.asList(new Object[][] { 
                { 1, "1", Types.INTEGER }, 
                { Integer.valueOf(69), "69", Types.INTEGER }, 
                { true, "t", Types.BOOLEAN },
                { ARRAY( 1, 2, 3, 4 ).asJdbcArray("int4"), "{1,2,3,4}", Types.ARRAY }, 
                { ARRAY( null, 2, 3, 4 ).asJdbcArray("int4"), "{NULL,2,3,4}", Types.ARRAY },
                { ARRAY( "a", "b" ).asJdbcArray("text"), "{a,b}", Types.ARRAY },
                { ARRAY( "first element", "second \"quoted\" element" ).asJdbcArray("text"), "{\"first element\",\"second \\\"quoted\\\" element\"}", Types.ARRAY },
                { ROW(1, 2).asPGobject("int_duplet"), "(1,2)", Types.OTHER },
                { ROW(1, 2, ARRAY("a", "b")).asPGobject("int_duplet_with_text_array"), "(1,2,\"{a,b}\")", Types.OTHER },
                { ROW("a", "b", new int[] { 1, 2, 3, 4 }), "(a,b,\"{1,2,3,4}\")", Types.OTHER },
                { ROW("a", null, ARRAY(ROW(1), ROW(2), null)), "(a,,\"{(1),(2),NULL}\")", Types.OTHER },
                { ROW("a", null, ARRAY(ROW(1, 11), ROW(2, 22), null)), "(a,,\"{\"\"(1,11)\"\",\"\"(2,22)\"\",NULL}\")", Types.OTHER },

        });
    }

    @Before
    public void createNeededTypes() throws SQLException {
        this.connection.prepareStatement("CREATE TYPE int_duplet AS (a int, b int);").execute();
        this.connection.prepareStatement("CREATE TYPE int_duplet_with_text_array AS (a int, b int, c text[]);").execute();
    }

    @After
    public void dropUsedTypes() throws SQLException {
        this.connection.prepareStatement("DROP TYPE int_duplet_with_text_array;").execute();
        this.connection.prepareStatement("DROP TYPE int_duplet;").execute();
    }

    @Test
    public void serializationTest() throws SerializationError {
        assertThat(SerializationUtils.toPgString(this.objectToSerialize), is(this.expectedString));
    }

    @Test
    public void passingParametersToQueryTest() {
        assertThat(
                template.queryForObject("SELECT (?)::text", new Object[] { this.objectToSerialize }, new int[] { this.expectedSQLType }, String.class),
                is(this.expectedString));
    }

    @Test
    public void passingParametersToSprocTest() {
        assertThat(
                template.queryForObject("SELECT from_int_duplet(?);", new Object[] { this.objectToSerialize }, new int[] { this.expectedSQLType }, Integer.class),
                is(1));
    }
}
