package sk.krayo.interviewproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import sk.krayo.interviewproject.apimodel.ColumnStats;
import sk.krayo.interviewproject.apimodel.TableStats;
import sk.krayo.interviewproject.service.DataSourceService;

import java.util.List;

import static sk.krayo.interviewproject.controller.DatabaseConnectionDataController.DB_PREFIX;
import static sk.krayo.interviewproject.controller.DatabaseMetaDataController.SCHEMA_PREFIX;
import static sk.krayo.interviewproject.controller.DatabaseMetaDataController.TABLE_PREFIX;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping(DB_PREFIX + SCHEMA_PREFIX + TABLE_PREFIX)
public class DatabaseMetaDataStatsController {

    @Autowired
    DataSourceService dataSourceService;

    @GetMapping("/stats")
    public TableStats getTableStats(@PathVariable("databaseId") long databaseId, @PathVariable("schemaName") String schemaName,
                                    @PathVariable("tableName") String tableName) {

        JdbcTemplate select = new JdbcTemplate(dataSourceService.buildDataSource(databaseId));

        // values count
        String sql = "SELECT COUNT(*) FROM " + schemaName + "." + tableName;
        Integer rowsCount = select.queryForObject(sql, Integer.class);
        TableStats tableStats = new TableStats();
        if (rowsCount != null) {
            tableStats.setNumberOfRecords(rowsCount);
        }
        sql = "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = '" + schemaName + "' AND table_name = '" + tableName + "';";
        Integer columnsCount = select.queryForObject(sql, Integer.class);
        if (columnsCount != null) {
            tableStats.setNumberOfAttributes(columnsCount);
        }
        return tableStats;
    }

    @GetMapping("/columns/{columnName}/stats")
    public ColumnStats getColumnStats(@PathVariable("databaseId") long databaseId, @PathVariable("schemaName") String schemaName,
                                      @PathVariable("tableName") String tableName, @PathVariable("columnName") String columnName) {

        String sql = "SELECT " + columnName + " FROM " + schemaName + "." + tableName + " ORDER BY " + columnName + " ASC";
        JdbcTemplate select = new JdbcTemplate(dataSourceService.buildDataSource(databaseId));
        List<String> columnValues = select.queryForList(sql, String.class);
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
                sql = "SELECT AVG(" + columnName + ") FROM " + schemaName + "." + tableName;
                String average = select.queryForObject(sql, String.class);
                columnStats.setAvg(average);
            } catch (Exception e) {
                columnStats.setAvg("N/A");
            }
        }
        return columnStats;
    }
}
