package sk.krayo.interviewproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.krayo.interviewproject.domain.DatabaseConnectionData;
import java.util.List;

@Repository
public interface DatabaseConnectionDataRepository extends JpaRepository<DatabaseConnectionData, Long> {
    List<DatabaseConnectionData> findByDatabaseName(String databaseName);
}
