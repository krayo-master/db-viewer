package sk.krayo.interviewproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import sk.krayo.interviewproject.apimodel.Column;
import sk.krayo.interviewproject.apimodel.ForeignKey;
import sk.krayo.interviewproject.apimodel.Table;
import sk.krayo.interviewproject.service.DataSourceService;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sk.krayo.interviewproject.controller.DatabaseConnectionDataController.DB_PREFIX;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping(DB_PREFIX)
public class DatabaseMetaDataController {

    protected static final String SCHEMA_PREFIX = "/schemas/{schemaName}";
    protected static final String TABLE_PREFIX = "/tables/{tableName}";

    @Autowired
    DataSourceService dataSourceService;

    @GetMapping("/schemas")
    public List<String> getSchemaNames(@PathVariable("databaseId") long databaseId) {
        List<String> schemaNames = new ArrayList<>();
        try {
            ResultSet resultSet = dataSourceService.buildDataSource(databaseId)
                    .getConnection().getMetaData().getSchemas();
            while (resultSet.next()) {
                schemaNames.add(resultSet.getString("TABLE_SCHEM"));
            }
            return schemaNames;
        } catch (SQLException ex) {
            System.out.println("failed get schemas");
        }
        return schemaNames;
    }

    @GetMapping(SCHEMA_PREFIX + "/tables")
    public List<String> getTableNames(@PathVariable("databaseId") long databaseId, @PathVariable("schemaName") String schemaName) {
        List<String> tableNames = new ArrayList<>();
        try {
            ResultSet resultSet = dataSourceService.buildDataSource(databaseId)
                    .getConnection().getMetaData().getTables(null, schemaName, null, null);
            while (resultSet.next()) {
                tableNames.add(resultSet.getString("TABLE_NAME"));
            }
            return tableNames;
        } catch (SQLException ex) {
            System.out.println("failed get tables");
        }
        return tableNames;
    }


    @GetMapping(SCHEMA_PREFIX + TABLE_PREFIX)
    public Table getColumns(@PathVariable("databaseId") long databaseId, @PathVariable("schemaName") String schemaName,
                            @PathVariable("tableName") String tableName) throws Exception {
        Table table = new Table();
        try {
            DatabaseMetaData databaseMetaData = dataSourceService.buildDataSource(databaseId).getConnection().getMetaData();
            ResultSet resultSet = databaseMetaData.getColumns(null, schemaName, tableName, null);
            table.setName(tableName);
            while (resultSet.next()) {
                Column column = new Column();
                column.setName(resultSet.getString("COLUMN_NAME"));
                column.setSize(resultSet.getString("COLUMN_SIZE"));
                column.setDatatype(resultSet.getString("TYPE_NAME"));
                column.setIsNullable(resultSet.getString("IS_NULLABLE"));
                column.setIsAutoIncrement(resultSet.getString("IS_AUTOINCREMENT"));
                table.getColumnList().add(column);
            }
            /* setting primary key */
            resultSet = databaseMetaData.getPrimaryKeys(null, null, tableName);
            while (resultSet.next()) {
                table.getPrimaryKey().add(resultSet.getString("COLUMN_NAME"));
            }
            /* setting foreign keys */
            resultSet = databaseMetaData.getImportedKeys(null, null, tableName);
            ForeignKey foreignKey;
            while (resultSet.next()) {
                foreignKey = new ForeignKey();
                foreignKey.setForeignKeyColumnName(resultSet.getString("FKCOLUMN_NAME"));
                foreignKey.setConnectedTableColumnName(resultSet.getString("PKTABLE_NAME") + "."
                        + resultSet.getString("PKCOLUMN_NAME"));
                table.getForeignKeys().add(foreignKey);
            }
        } catch (SQLException ex) {
            System.out.println("failed columns");
        }
        return table;
    }

    @GetMapping(SCHEMA_PREFIX + TABLE_PREFIX + "/preview")
    public List<Map<String, Object>> previewTable(@PathVariable("databaseId") long databaseId, @PathVariable("schemaName") String schemaName,
                                                  @PathVariable("tableName") String tableName) {

        String sql = "SELECT * FROM " + schemaName + "." + tableName;
        JdbcTemplate select = new JdbcTemplate(dataSourceService.buildDataSource(databaseId));
        return select.queryForList(sql);
    }
}
