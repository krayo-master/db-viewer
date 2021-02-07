package sk.krayo.dbproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import sk.krayo.dbproject.apimodel.Column;
import sk.krayo.dbproject.apimodel.ForeignKey;
import sk.krayo.dbproject.apimodel.Table;
import sk.krayo.dbproject.exception.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseMetaDataService {

    @Autowired
    DataSourceService dataSourceService;

    public List<String> getSchemaNames(long databaseId) {
        List<String> schemaNames = new ArrayList<>();
        try (Connection connection = dataSourceService.buildDataSource(databaseId).getConnection()){
            ResultSet resultSet = connection.getMetaData().getSchemas();
            while (resultSet.next()) {
                schemaNames.add(resultSet.getString("TABLE_SCHEM"));
            }
        } catch (SQLException ex) {
            throw new DatabaseConnectionException(databaseId);
        }
        return schemaNames;
    }

    public List<String> getTableNames(long databaseId, String schemaName) {
        List<String> tableNames = new ArrayList<>();
        try (Connection connection = dataSourceService.buildDataSource(databaseId).getConnection()){
            ResultSet resultSet = connection.getMetaData().getTables(null, schemaName, null, null);
            while (resultSet.next()) {
                tableNames.add(resultSet.getString("TABLE_NAME"));
            }
        } catch (SQLException ex) {
            throw new DatabaseConnectionException(databaseId);
        }
        return tableNames;
    }

    public Table getColumns(long databaseId, String schemaName, String tableName) {
        Table table = new Table();
        try (Connection connection = dataSourceService.buildDataSource(databaseId).getConnection()){
            DatabaseMetaData databaseMetaData = connection.getMetaData();
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
            throw new DatabaseConnectionException(databaseId);
        }
        return table;
    }


    public List<Map<String, Object>> previewTable(long databaseId, String schemaName, String tableName) {
        String sql = "SELECT * FROM " + schemaName + "." + tableName;
        JdbcTemplate select = new JdbcTemplate(dataSourceService.buildDataSource(databaseId));
        return select.queryForList(sql);
    }
}
