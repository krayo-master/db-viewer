package sk.krayo.interviewproject;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sk.krayo.interviewproject.controller.DatabaseConnectionDataController;
import sk.krayo.interviewproject.controller.DatabaseMetaDataController;
import sk.krayo.interviewproject.controller.DatabaseMetaDataStatsController;
import sk.krayo.interviewproject.service.DataSourceService;


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
