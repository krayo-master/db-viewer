package sk.krayo.dbproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import sk.krayo.dbproject.apimodel.ColumnStats;
import sk.krayo.dbproject.apimodel.TableStats;
import sk.krayo.dbproject.dao.DatabaseMetaDataStatsDao;

import java.util.List;

@Service
public class DatabaseMetaDataStatsService {

    @Autowired
    DatabaseMetaDataStatsDao databaseMetaDataStatsDao;

    public TableStats getTableStats(JdbcTemplate jdbcTemplate, String schemaName, String tableName) {
        Integer rowsCount = databaseMetaDataStatsDao.getTableRowsCount(jdbcTemplate, schemaName, tableName);
        TableStats tableStats = new TableStats();
        if (rowsCount != null) {
            tableStats.setNumberOfRecords(rowsCount);
        }
        Integer columnsCount = databaseMetaDataStatsDao.getNumberOfAttributes(jdbcTemplate, schemaName, tableName);
        if (columnsCount != null) {
            tableStats.setNumberOfAttributes(columnsCount);
        }
        return tableStats;
    }

    public ColumnStats getColumnStats(JdbcTemplate jdbcTemplate, String schemaName, String tableName, String columnName) {
        List<String> columnValues = databaseMetaDataStatsDao.getColumnValues(jdbcTemplate, schemaName, tableName, columnName);
        ColumnStats columnStats = new ColumnStats();
        if (!columnValues.isEmpty()) {
            int columnSize = columnValues.size();
            columnStats.setMin(columnValues.get(0));
            columnStats.setMax(columnValues.get(columnSize - 1));
            String middleValue = columnValues.get(columnSize / 2);
            if (columnSize % 2 != 0) {
                columnStats.setMedian(middleValue);
            } else {
                if (middleValue.equals(columnValues.get((columnSize / 2) - 1))) {
                    columnStats.setMedian(middleValue);
                }
                columnStats.setMedian("N/A");
            }
            try {
                columnStats.setAvg(databaseMetaDataStatsDao.getColumnAverage(jdbcTemplate, schemaName, tableName, columnName));
            } catch (Exception e) {
                columnStats.setAvg("N/A");
            }
        }
        return columnStats;
    }
}
