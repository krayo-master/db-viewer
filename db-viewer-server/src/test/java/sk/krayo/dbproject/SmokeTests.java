package sk.krayo.dbproject;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sk.krayo.dbproject.controller.DatabaseConnectionDataController;
import sk.krayo.dbproject.controller.DatabaseMetaDataController;
import sk.krayo.dbproject.controller.DatabaseMetaDataStatsController;
import sk.krayo.dbproject.service.DataSourceService;


@SpringBootTest
public class SmokeTests {

    @Autowired
    private DatabaseConnectionDataController databaseConnectionDataController;

    @Autowired
    private DatabaseMetaDataController databaseMetaDataController;

    @Autowired
    private DatabaseMetaDataStatsController databaseMetaDataStatsController;

    @Autowired
    private DataSourceService dataSourceService;

    @Test
    public void contextLoads() throws Exception {
        assertThat(databaseConnectionDataController).isNotNull();
        assertThat(databaseMetaDataController).isNotNull();
        assertThat(databaseMetaDataStatsController).isNotNull();
        assertThat(dataSourceService).isNotNull();
    }
}
