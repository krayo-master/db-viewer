package sk.krayo.dbproject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit4.SpringRunner;
import sk.krayo.dbproject.apimodel.ColumnStats;
import sk.krayo.dbproject.apimodel.Table;
import sk.krayo.dbproject.apimodel.TableStats;
import sk.krayo.dbproject.controller.DatabaseMetaDataController;
import sk.krayo.dbproject.controller.DatabaseMetaDataStatsController;

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

    static boolean dataLoaded = false;
    static final long TEST_DB_ID = 9999;
    static final String PUBLIC_SCHEMA = "public";
    static final String TABLE_NAME = "database_connection_data";
    static final String TEST_COLUMN_NAME = "id";

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
    public void testGetColumns(){
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
        assertThat(tableStats.getNumberOfAttributes()).isPositive();
        assertThat(tableStats.getNumberOfRecords()).isPositive();
    }

    @Test
    public void testGetColumnStatsInt() {
        ColumnStats columnStats = databaseMetaDataStatsController.getColumnStats(TEST_DB_ID, PUBLIC_SCHEMA, TABLE_NAME, TEST_COLUMN_NAME);
        assertThat(Double.valueOf(columnStats.getMin())).isPositive();
        assertThat(Double.valueOf(columnStats.getMax())).isPositive();
        assertThat(Double.valueOf(columnStats.getAvg())).isPositive();
        assertThat(Double.valueOf(columnStats.getMedian())).isPositive();
    }
}