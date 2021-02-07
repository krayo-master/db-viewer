package sk.krayo.dbproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.krayo.dbproject.apimodel.Table;
import sk.krayo.dbproject.service.DatabaseMetaDataService;
import java.util.List;
import java.util.Map;

import static sk.krayo.dbproject.controller.DatabaseConnectionDataController.DB_PREFIX;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping(DB_PREFIX)
public class DatabaseMetaDataController {

    protected static final String SCHEMA_PREFIX = "/schemas/{schemaName}";
    protected static final String TABLE_PREFIX = "/tables/{tableName}";

    @Autowired
    DatabaseMetaDataService databaseMetaDataService;

    @GetMapping("/schemas")
    public List<String> getSchemaNames(@PathVariable("databaseId") long databaseId) {
        return databaseMetaDataService.getSchemaNames(databaseId);
    }

    @GetMapping(SCHEMA_PREFIX + "/tables")
    public List<String> getTableNames(@PathVariable("databaseId") long databaseId, @PathVariable("schemaName") String schemaName) {
        return databaseMetaDataService.getTableNames(databaseId, schemaName);
    }


    @GetMapping(SCHEMA_PREFIX + TABLE_PREFIX)
    public Table getColumns(@PathVariable("databaseId") long databaseId, @PathVariable("schemaName") String schemaName,
                            @PathVariable("tableName") String tableName) {
        return databaseMetaDataService.getColumns(databaseId, schemaName, tableName);
    }

    @GetMapping(SCHEMA_PREFIX + TABLE_PREFIX + "/preview")
    public List<Map<String, Object>> previewTable(@PathVariable("databaseId") long databaseId, @PathVariable("schemaName") String schemaName,
                                                  @PathVariable("tableName") String tableName) {
        return databaseMetaDataService.previewTable(databaseId, schemaName, tableName);
    }
}
