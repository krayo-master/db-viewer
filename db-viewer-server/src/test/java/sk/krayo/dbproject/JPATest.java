package sk.krayo.dbproject;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit4.SpringRunner;
import sk.krayo.dbproject.domain.DatabaseConnectionData;
import sk.krayo.dbproject.repository.DatabaseConnectionDataRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class JPATest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    DatabaseConnectionDataRepository repository;

    @Autowired
    DataSource dataSource;

    @Before
    public void setup() throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(con, new ClassPathResource("/Clean.sql"));
        }
    }

    @Test
    public void should_find_no_databaseConnectionData_if_repository_is_empty() {
        Iterable<DatabaseConnectionData> databaseConnectionData = repository.findAll();
        assertThat(databaseConnectionData).isEmpty();
    }

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

    @Test
    public void should_find_all_databaseConnectionData() {
        DatabaseConnectionData db1 = new DatabaseConnectionData("testDb#1", "testHost#1", 1234,
                "testDbName#1","testDbUsername#1","testDbPassword#1");
        entityManager.persist(db1);

        DatabaseConnectionData db2 = new DatabaseConnectionData("testDb#2", "testHost#2", 4321,
                "testDbName#2","testDbUsername#2","testDbPassword#2");
        entityManager.persist(db2);

        DatabaseConnectionData db3 = new DatabaseConnectionData("testDb#3", "testHost#3", 1234,
                "testDbName#3","testDbUsername#3","testDbPassword#3");
        entityManager.persist(db3);

        Iterable<DatabaseConnectionData> databaseConnectionData = repository.findAll();

        assertThat(databaseConnectionData).hasSize(3).contains(db1, db2, db3);
    }

    @Test
    public void should_find_databaseConnectionData_by_id() {
        DatabaseConnectionData db1 = new DatabaseConnectionData("testDb#1", "testHost#1", 1234,
                "testDbName#1","testDbUsername#1","testDbPassword#1");
        entityManager.persist(db1);

        DatabaseConnectionData db2 = new DatabaseConnectionData("testDb#2", "testHost#2", 4321,
                "testDbName#2","testDbUsername#2","testDbPassword#2");
        entityManager.persist(db2);

        DatabaseConnectionData foundDatabaseConnectionData = repository.findById(db2.getId()).get();

        assertThat(foundDatabaseConnectionData).isEqualTo(db2);
    }

    @Test
    public void should_find_published_databaseConnectionData() {
        DatabaseConnectionData db1 = new DatabaseConnectionData("testDb#1", "testHost#1", 1234,
                "testDbName#1","testDbUsername#1","testDbPassword#1");
        entityManager.persist(db1);

        DatabaseConnectionData db2 = new DatabaseConnectionData("testDb#2", "testHost#2", 4321,
                "testDbName#2","testDbUsername#2","testDbPassword#2");
        entityManager.persist(db2);

        //same databaseName as db1
        DatabaseConnectionData db3 = new DatabaseConnectionData("testDb#3", "testHost#3", 1234,
                "testDbName#1","testDbUsername#3","testDbPassword#3");
        entityManager.persist(db3);

        Iterable<DatabaseConnectionData> databaseConnectionData = repository.findByDatabaseName("testDbName#1");

        assertThat(databaseConnectionData).hasSize(2).contains(db1, db3);
    }

    @Test
    public void should_update_databaseConnectionData_by_id() {
        DatabaseConnectionData db1 = new DatabaseConnectionData("testDb#1", "testHost#1", 1234,
                "testDbName#1","testDbUsername#1","testDbPassword#1");;
        entityManager.persist(db1);

        DatabaseConnectionData db2 = new DatabaseConnectionData("testDb#2", "testHost#2", 4321,
                "testDbName#2","testDbUsername#2","testDbPassword#2");
        entityManager.persist(db2);

        DatabaseConnectionData updateddb = new DatabaseConnectionData("testDb#3", "testHost#3", 1234,
                "testDbName#3","testDbUsername#3","testDbPassword#3");

        DatabaseConnectionData db = repository.findById(db2.getId()).get();
        db.setName(updateddb.getName());
        db.setHostname(updateddb.getHostname());
        db.setPort(updateddb.getPort());
        db.setDatabaseName(updateddb.getDatabaseName());
        db.setUsername(updateddb.getUsername());
        db.setPassword(updateddb.getPassword());
        repository.save(db);

        DatabaseConnectionData checkdb = repository.findById(db2.getId()).get();

        assertThat(checkdb.getId()).isEqualTo(db2.getId());
        assertThat(checkdb.getName()).isEqualTo(updateddb.getName());
        assertThat(checkdb.getHostname()).isEqualTo(updateddb.getHostname());
        assertThat(checkdb.getPort()).isEqualTo(updateddb.getPort());
        assertThat(checkdb.getDatabaseName()).isEqualTo(updateddb.getDatabaseName());
        assertThat(checkdb.getUsername()).isEqualTo(updateddb.getUsername());
        assertThat(checkdb.getPassword()).isEqualTo(updateddb.getPassword());
    }

    @Test
    public void should_delete_databaseConnectionData_by_id() {
        DatabaseConnectionData db1 = new DatabaseConnectionData("testDb#1", "testHost#1", 1234,
                "testDbName#1","testDbUsername#1","testDbPassword#1");
        entityManager.persist(db1);

        DatabaseConnectionData db2 = new DatabaseConnectionData("testDb#2", "testHost#2", 4321,
                "testDbName#2","testDbUsername#2","testDbPassword#2");
        entityManager.persist(db2);

        DatabaseConnectionData db3 = new DatabaseConnectionData("testDb#3", "testHost#3", 1234,
                "testDbName#3","testDbUsername#3","testDbPassword#3");
        entityManager.persist(db3);

        repository.deleteById(db2.getId());

        Iterable<DatabaseConnectionData> databaseConnectionData = repository.findAll();

        assertThat(databaseConnectionData).hasSize(2).contains(db1, db3);
    }

    @Test
    public void should_delete_all_databaseConnectionData() {
        entityManager.persist(new DatabaseConnectionData("testDb#1", "testHost#1", 1234,
                "testDbName#1","testDbUsername#1","testDbPassword#1"));
        entityManager.persist(new DatabaseConnectionData("testDb#2", "testHost#2", 4321,
                "testDbName#2","testDbUsername#2","testDbPassword#2"));

        repository.deleteAll();

        assertThat(repository.findAll()).isEmpty();
    }
}
