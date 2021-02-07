package sk.krayo.dbproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import sk.krayo.dbproject.apimodel.ColumnStats;
import sk.krayo.dbproject.apimodel.TableStats;
import sk.krayo.dbproject.service.DataSourceService;
import sk.krayo.dbproject.service.DatabaseMetaDataStatsService;

import static sk.krayo.dbproject.controller.DatabaseConnectionDataController.DB_PREFIX;
import static sk.krayo.dbproject.controller.DatabaseMetaDataController.SCHEMA_PREFIX;
import static sk.krayo.dbproject.controller.DatabaseMetaDataController.TABLE_PREFIX;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping(DB_PREFIX + SCHEMA_PREFIX + TABLE_PREFIX)
public class DatabaseMetaDataStatsController {

    @Autowired
    DatabaseMetaDataStatsService databaseMetaDataStatsService;

    @Autowired
    DataSourceService dataSourceService;

    @GetMapping("/stats")
    public TableStats getTableStats(@PathVariable("databaseId") long databaseId, @PathVariable("schemaName") String schemaName,
                                    @PathVariable("tableName") String tableName) {
        return databaseMetaDataStatsService.getTableStats(
                new JdbcTemplate(dataSourceService.buildDataSource(databaseId)), schemaName, tableName);
    }

    /**
     *
     */
    @GetMapping("/columns/{columnName}/stats")
    public ColumnStats getColumnStats(@PathVariable("databaseId") long databaseId, @PathVariable("schemaName") String schemaName,
                                      @PathVariable("tableName") String tableName, @PathVariable("columnName") String columnName) {
        return databaseMetaDataStatsService.getColumnStats(
                new JdbcTemplate(dataSourceService.buildDataSource(databaseId)), schemaName, tableName, columnName);
    }
}
