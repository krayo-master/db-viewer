package sk.krayo.dbproject.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DatabaseMetaDataStatsDao {

    public Integer getTableRowsCount(JdbcTemplate jdbcTemplate, String schemaName, String tableName){
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + schemaName + "." + tableName, Integer.class);
    }

    public Integer getNumberOfAttributes(JdbcTemplate jdbcTemplate, String schemaName, String tableName){
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = '"
                + schemaName + "' AND table_name = '" + tableName + "';", Integer.class);
    }

    public List<String> getColumnValues(JdbcTemplate jdbcTemplate, String schemaName, String tableName, String columnName){
        return jdbcTemplate.queryForList("SELECT " + columnName + " FROM " + schemaName + "." + tableName
                + " ORDER BY " + columnName + " ASC", String.class);
    }

    public String getColumnAverage(JdbcTemplate jdbcTemplate, String schemaName, String tableName, String columnName){
        return jdbcTemplate.queryForObject("SELECT AVG(" + columnName + ") FROM " + schemaName + "."
                + tableName, String.class);
    }
}
