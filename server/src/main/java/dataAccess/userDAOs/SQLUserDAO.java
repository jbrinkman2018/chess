package dataAccess.userDAOs;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.User;
import org.springframework.security.core.parameters.P;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class SQLUserDAO implements UserDAO {
    public SQLUserDAO(){
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.configTable(createUserTable);
        }
        catch(DataAccessException ex) {
            System.out.println(ex.getStatusCode() + String.format(", \"message\" Error: %s", ex.getMessage()));
        }
    }
    @Override
    public User getUser(String username) throws DataAccessException {
        return new User("null", "null","null");
    }
    @Override
    public void createUser(User user) throws DataAccessException {

    }
    @Override
    public void clear(){

    }
    private final String[] createUserTable = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(password)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

}
