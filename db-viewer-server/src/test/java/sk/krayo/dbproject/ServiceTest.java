package sk.krayo.dbproject;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import sk.krayo.dbproject.domain.DatabaseConnectionData;
import sk.krayo.dbproject.repository.DatabaseConnectionDataRepository;
import sk.krayo.dbproject.service.DataSourceService;

public class ServiceTest {

    @Test
    public void testBuildDataSource() {
        IMocksControl ctxt = EasyMock.createControl();
        DatabaseConnectionDataRepository databaseConnectionDataRepository = ctxt.createMock(DatabaseConnectionDataRepository.class);
        DataSourceService victim = new DataSourceService();
        victim.setDatabaseConnectionDataRepository(databaseConnectionDataRepository);
        DatabaseConnectionData db1 = new DatabaseConnectionData("testDb", "testHost", 1234,
                "testDbName","testDbUsername","testDbPassword");

        EasyMock.expect(databaseConnectionDataRepository.findById(1L)).andReturn(java.util.Optional.of(db1));
        ctxt.replay();
        DriverManagerDataSource result = victim.buildDataSource(1L);
        ctxt.verify();

        Assert.assertEquals(result.getUsername(),"testDbUsername");
        Assert.assertEquals(result.getPassword(),"testDbPassword");
        ctxt.reset();
    }
}
