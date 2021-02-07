package sk.krayo.dbproject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit4.SpringRunner;
import sk.krayo.dbproject.dao.DatabaseMetaDataStatsDao;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import static sk.krayo.dbproject.MetaDataTest.*;
import static sk.krayo.dbproject.service.DataSourceService.POSTGRESQL_DRIVER;
import static sk.krayo.dbproject.service.DataSourceService.POSTGRESQL_URL;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(DatabaseMetaDataStatsDao.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DatabaseMetaDataStatsDaoTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    DatabaseMetaDataStatsDao databaseMetaDataStatsDao;

    private static JdbcTemplate jdbcTemplate;

    @BeforeClass
    public static void setJdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(POSTGRESQL_DRIVER);
        dataSource.setUrl(POSTGRESQL_URL + "localhost" + ":" + 5432 + "/" +
                "dbviewer");
        dataSource.setUsername("postgres");
        dataSource.setPassword("lalala");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

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
    public void testGetTableRowsCount() {
        Integer count = databaseMetaDataStatsDao.getTableRowsCount(jdbcTemplate, PUBLIC_SCHEMA, TABLE_NAME);
        Assert.assertNotNull(count);
        Assert.assertEquals(1, (int) count);
    }


    @Test
    public void testGetNumberOfAttributes() {
        Integer count = databaseMetaDataStatsDao.getNumberOfAttributes(jdbcTemplate, PUBLIC_SCHEMA, TABLE_NAME);
        Assert.assertNotNull(count);
        Assert.assertEquals(7, (int) count);
    }

    @Test
    public void testGetColumnValues() {
        List<String> values = databaseMetaDataStatsDao.getColumnValues(jdbcTemplate, PUBLIC_SCHEMA, TABLE_NAME, TEST_COLUMN_NAME);
        Assert.assertTrue(values.size() > 0);
        Assert.assertEquals(String.valueOf(TEST_DB_ID), values.get(0));

    }

    @Test
    public void testGetColumnAverage() {
        String average = databaseMetaDataStatsDao.getColumnAverage(jdbcTemplate, PUBLIC_SCHEMA, TABLE_NAME, TEST_COLUMN_NAME);
        Assert.assertNotNull(average);
        Assert.assertEquals(TEST_DB_ID + ".0000000000000000", average);
    }
}
