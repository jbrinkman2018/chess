package DataAccessException;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    private int statusCode;
    private String message;
// clean this up
    public DataAccessException(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }
    public DataAccessException(String message){
        super(message);
    }
    public int getStatusCode() {
        return statusCode;
    }
    @Override
    public String getMessage() {
        return this.message;
    }
}
