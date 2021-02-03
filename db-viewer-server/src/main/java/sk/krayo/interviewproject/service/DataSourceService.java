package sk.krayo.interviewproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import sk.krayo.interviewproject.domain.DatabaseConnectionData;
import sk.krayo.interviewproject.exception.ResourceNotFoundException;
import sk.krayo.interviewproject.repository.DatabaseConnectionDataRepository;

@Service
public class DataSourceService {

    public static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";
    public static final String POSTGRESQL_URL = "jdbc:postgresql://";


    private DatabaseConnectionDataRepository databaseConnectionDataRepository;

    @Autowired
    public void setDatabaseConnectionDataRepository(DatabaseConnectionDataRepository databaseConnectionDataRepository){
        this.databaseConnectionDataRepository = databaseConnectionDataRepository;
    }

    public DriverManagerDataSource buildDataSource(long databaseId) {
        DatabaseConnectionData databaseConnectionData = databaseConnectionDataRepository.findById(databaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Database Connection with id " + databaseId + " not found"));

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(POSTGRESQL_DRIVER);

        dataSource.setUrl(POSTGRESQL_URL + databaseConnectionData.getHostname() + ":" + databaseConnectionData.getPort() + "/" +
                databaseConnectionData.getDatabaseName());
        dataSource.setUsername(databaseConnectionData.getUsername());
        dataSource.setPassword(databaseConnectionData.getPassword());
        return dataSource;
    }
}
