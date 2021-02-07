package sk.krayo.dbproject;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import sk.krayo.dbproject.domain.DatabaseConnectionData;
import sk.krayo.dbproject.repository.DatabaseConnectionDataRepository;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class JPATest {

    @Autowired
    DatabaseConnectionDataRepository repository;

    @Test
    public void should_store_a_databaseConnectionData() {
        DatabaseConnectionData databaseConnectionData = repository 
                .save(new DatabaseConnectionData("testDb", "testDbHost", 1234,
                        "testDbName","testDbUsername", "testDbPassword"));

        assertThat(databaseConnectionData).hasFieldOrPropertyWithValue("name", "testDb");
        assertThat(databaseConnectionData).hasFieldOrPropertyWithValue("hostname", "testDbHost");
        assertThat(databaseConnectionData).hasFieldOrPropertyWithValue("port", 1234);
        assertThat(databaseConnectionData).hasFieldOrPropertyWithValue("databaseName", "testDbName");
        assertThat(databaseConnectionData).hasFieldOrPropertyWithValue("username", "testDbUsername");
        assertThat(databaseConnectionData).hasFieldOrPropertyWithValue("password", "testDbPassword");
    }
}
