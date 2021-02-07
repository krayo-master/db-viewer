package sk.krayo.dbproject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import sk.krayo.dbproject.apimodel.ColumnStats;
import sk.krayo.dbproject.apimodel.TableStats;
import sk.krayo.dbproject.dao.DatabaseMetaDataStatsDao;
import sk.krayo.dbproject.service.DatabaseMetaDataStatsService;
import java.util.ArrayList;
import java.util.List;

import static sk.krayo.dbproject.MetaDataTest.*;

@RunWith(SpringRunner.class)
public class DatabaseMetaDataStatsServiceTest {

    @InjectMocks
    DatabaseMetaDataStatsService databaseMetaDataStatsService;

    @Mock
    private DatabaseMetaDataStatsDao databaseMetaDataStatsDao;

    @Test
    public void testGetTableStats() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        Mockito.when(databaseMetaDataStatsDao.getTableRowsCount(jdbcTemplate, PUBLIC_SCHEMA, TABLE_NAME))
                .thenReturn(5);
        Mockito.when(databaseMetaDataStatsDao.getNumberOfAttributes(jdbcTemplate, PUBLIC_SCHEMA, TABLE_NAME))
                .thenReturn(5);
        TableStats tableStats = databaseMetaDataStatsService.getTableStats(jdbcTemplate, PUBLIC_SCHEMA, TABLE_NAME);
        Assert.assertEquals(5, tableStats.getNumberOfRecords());
        Assert.assertEquals(5, tableStats.getNumberOfAttributes());
    }


    @Test(expected = Exception.class)
    public void testGetColumnStats() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        List<String> values = new ArrayList<>();
        values.add("10");
        values.add("20");
        values.add("30");
        Mockito.when(databaseMetaDataStatsDao.getColumnValues(jdbcTemplate, PUBLIC_SCHEMA, TABLE_NAME, TEST_COLUMN_NAME))
                .thenReturn(values);
        Mockito.when(databaseMetaDataStatsDao.getColumnAverage(jdbcTemplate, PUBLIC_SCHEMA, TABLE_NAME, TEST_COLUMN_NAME))
                .thenReturn("15");
        ColumnStats columnStats = databaseMetaDataStatsService.getColumnStats(jdbcTemplate, PUBLIC_SCHEMA, TABLE_NAME, TEST_COLUMN_NAME);
        Assert.assertEquals(values.get(0), columnStats.getMin());
        Assert.assertEquals(values.get(2), columnStats.getMax());
        Assert.assertEquals("20", columnStats.getMedian());
        Assert.assertEquals("15", columnStats.getAvg());

        // median and avg not applicable
        values.add("TestString");
        Mockito.when(databaseMetaDataStatsDao.getColumnAverage(jdbcTemplate, PUBLIC_SCHEMA, TABLE_NAME, TEST_COLUMN_NAME))
                .thenThrow(Exception.class);
        columnStats = databaseMetaDataStatsService.getColumnStats(jdbcTemplate, PUBLIC_SCHEMA, TABLE_NAME, TEST_COLUMN_NAME);
        Assert.assertEquals("N/A", columnStats.getMedian());
        Assert.assertEquals("N/A", columnStats.getAvg());
    }
}
