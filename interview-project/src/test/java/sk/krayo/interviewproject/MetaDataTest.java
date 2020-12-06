package sk.krayo.interviewproject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit4.SpringRunner;
import sk.krayo.interviewproject.apimodel.ColumnStats;
import sk.krayo.interviewproject.apimodel.Table;
import sk.krayo.interviewproject.apimodel.TableStats;
import sk.krayo.interviewproject.controller.DatabaseMetaDataController;
import sk.krayo.interviewproject.controller.DatabaseMetaDataStatsController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MetaDataTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    DatabaseMetaDataController databaseMetaDataController;

    @Autowired
    DatabaseMetaDataStatsController databaseMetaDataStatsController;

    private static boolean dataLoaded = false;
    private static final long TEST_DB_ID = 9999;
    private static final String PUBLIC_SCHEMA = "public";
    private static final String TABLE_NAME = "database_connection_data";
    private static final String TEST_COLUMN_NAME = "id";

    @Before
    public void setup() throws SQLException {
        if (!dataLoaded) {
            try (Connection con = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(con, new ClassPathResource("/MetaDataTestSql.sql"));
                dataLoaded = true;
            }
        }
    }

    @Test
    public void testGetSchemaNames() {
        List<String> schemas = databaseMetaDataController.getSchemaNames(TEST_DB_ID);
        assertThat(schemas).isNotEmpty().contains(PUBLIC_SCHEMA);
    }

    @Test
    public void testGetTableNames() {
        List<String> tables = databaseMetaDataController.getTableNames(TEST_DB_ID, PUBLIC_SCHEMA);
        assertThat(tables).isNotEmpty().contains(TABLE_NAME);
    }

    @Test
    public void testGetColumns() throws Exception {
        Table table = databaseMetaDataController.getColumns(TEST_DB_ID, PUBLIC_SCHEMA, TABLE_NAME);
        assertThat(table.getColumnList()).isNotEmpty();
        assertThat(table.getColumnList().get(0).getName()).isEqualTo(TEST_COLUMN_NAME);
    }

    @Test
    public void testPreviewTable() {
        List<Map<String, Object>> previewTable = databaseMetaDataController.previewTable(TEST_DB_ID, PUBLIC_SCHEMA, TABLE_NAME);
        assertThat(previewTable).isNotEmpty();
        assertThat(previewTable.get(0).get(TEST_COLUMN_NAME)).isEqualTo(TEST_DB_ID);
    }

    @Test
    public void testGetTableStats() {
        TableStats tableStats = databaseMetaDataStatsController.getTableStats(TEST_DB_ID, PUBLIC_SCHEMA, TABLE_NAME);
        assertThat(tableStats.getNumberOfAttributes()).isGreaterThan(0);
        assertThat(tableStats.getNumberOfRecords()).isGreaterThan(0);
    }

    @Test
    public void testGetColumnStatsInt() {
        ColumnStats columnStats = databaseMetaDataStatsController.getColumnStats(TEST_DB_ID, PUBLIC_SCHEMA, TABLE_NAME, TEST_COLUMN_NAME);
        assertThat(Double.valueOf(columnStats.getMin())).isGreaterThan(0);
        assertThat(Double.valueOf(columnStats.getMax())).isGreaterThan(0);
        assertThat(Double.valueOf(columnStats.getAvg())).isGreaterThan(0);
        assertThat(Double.valueOf(columnStats.getMedian())).isGreaterThan(0);
    }
}