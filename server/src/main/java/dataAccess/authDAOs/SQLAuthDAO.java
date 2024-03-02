package dataAccess.authDAOs;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.*;

public class SQLAuthDAO implements AuthDAO {
    public SQLAuthDAO(){
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.configTable(createAuthTable);
        }
        catch(DataAccessException ex) {
            System.out.println(ex.getStatusCode() + String.format(", \"message\" Error: %s", ex.getMessage()));
        }
    }

    @Override
    public Auth createAuth(String username) {
        String sqlCreateAuth = "INSERT INTO auth (name, type, json) VALUES (?, ?, ?)";
                "" +
                        "" +
                "";
        return new Auth("null", "null");
    }
    @Override
    public void deleteAuth(String authToken){}
    @Override
    public Auth getAuth(String authToken){
        return new Auth("null", "null");
    }
    @Override
    public void clear(){
    }
    private final String[] createAuthTable = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `id` int NOT NULL AUTO_INCREMENT,
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
