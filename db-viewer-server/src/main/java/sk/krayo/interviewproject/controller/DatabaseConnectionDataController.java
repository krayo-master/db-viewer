package sk.krayo.interviewproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.krayo.interviewproject.domain.DatabaseConnectionData;
import sk.krayo.interviewproject.exception.ResourceNotFoundException;
import sk.krayo.interviewproject.repository.DatabaseConnectionDataRepository;

import java.util.ArrayList;
import java.util.List;

import static sk.krayo.interviewproject.controller.DatabaseConnectionDataController.*;


/**
 * rest api calls for getting, adding, updating and deleting Database connection data
 */
@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping(BASE_PREFIX + "/databases")
public class DatabaseConnectionDataController {

    protected static final String BASE_PREFIX = "/api";
    protected static final String DB_PREFIX = BASE_PREFIX + "/databases/{databaseId}";

    @Autowired
    DatabaseConnectionDataRepository databaseConnectionDataRepository;

    @GetMapping()
    public ResponseEntity<List<DatabaseConnectionData>> getAllDatabaseConnectionData(@RequestParam(required = false) String databaseName) {
        List<DatabaseConnectionData> databaseConnectionDataList = new ArrayList<>();

        if (databaseName == null)
            databaseConnectionDataList.addAll(databaseConnectionDataRepository.findAll());
        else
            databaseConnectionDataList.addAll(databaseConnectionDataRepository.findByDatabaseName(databaseName));

        return new ResponseEntity<>(databaseConnectionDataList, HttpStatus.OK);
    }

    @GetMapping("/{databaseId}")
    public ResponseEntity<DatabaseConnectionData> getDatabaseConnectionDataById(@PathVariable("databaseId") long databaseId) {
        DatabaseConnectionData databaseConnectionDataData = databaseConnectionDataRepository.findById(databaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Database Connection with id " + databaseId + " not found"));
        return new ResponseEntity<>(databaseConnectionDataData, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DatabaseConnectionData> createDatabaseConnectionData(@RequestBody DatabaseConnectionData databaseConnectionData) {
        DatabaseConnectionData _databaseConnectionData = databaseConnectionDataRepository
                .save(databaseConnectionData);
        return new ResponseEntity<>(_databaseConnectionData, HttpStatus.CREATED);
    }

    @PutMapping("/{databaseId}")
    public ResponseEntity<DatabaseConnectionData> updateDatabaseConnectionData(@PathVariable("databaseId") long databaseId, @RequestBody DatabaseConnectionData databaseConnectionData) {
        DatabaseConnectionData _databaseConnectionDataData = databaseConnectionDataRepository.findById(databaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Database Connection with id " + databaseId + " not found"));
        return new ResponseEntity<>(databaseConnectionDataRepository.save(databaseConnectionData), HttpStatus.OK);
    }

    @DeleteMapping("/{databaseId}")
    public ResponseEntity<HttpStatus> deleteDatabaseConnectionData(@PathVariable("databaseId") long databaseId) {
        databaseConnectionDataRepository.deleteById(databaseId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllDatabaseConnectionData() {
        databaseConnectionDataRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
