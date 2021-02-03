package sk.krayo.dbproject.exception;

public class DatabaseConnectionException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public DatabaseConnectionException(String msg) {
        super(msg);
    }
}
