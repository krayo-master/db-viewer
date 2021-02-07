package sk.krayo.dbproject.exception;

public class DatabaseConnectionException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public DatabaseConnectionException(long databaseId) {
        super("Connection error for database id: " + databaseId);
    }
}
