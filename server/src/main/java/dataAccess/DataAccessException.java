package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    private int statusCode;

    public DataAccessException(int statusCode, String message) {

        this.statusCode = statusCode;
    }
    public int getStatusCode() {
        return statusCode;
    }
}
