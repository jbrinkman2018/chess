package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    private int statusCode;
    private String message;

    public DataAccessException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
    public int getStatusCode() {
        return statusCode;
    }
    @Override
    public String getMessage() {
        return this.message;
    }
}
