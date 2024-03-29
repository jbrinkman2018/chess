package dataAccess.userDAOs;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.User;
import org.springframework.security.core.parameters.P;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.Map;

public class SQLUserDAO implements UserDAO {
    public SQLUserDAO(){
        try {
            DatabaseManager.configTable(createUserTable);
        }
        catch(DataAccessException ex) {
            System.out.println(ex.getStatusCode() + String.format(", \"message\" Error: %s", ex.getMessage()));
        }
    }
    @Override
    public User getUser(String username) throws DataAccessException {
        String SQLGetUser = "SELECT * FROM user WHERE username = '" + username + "'";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(SQLGetUser)) {
                ResultSet rs = preparedStatement.executeQuery();
                User user = null;
                if (rs.next()) {
                    user = new User(rs.getString(1), rs.getString(2), rs.getString(3));
                }
                return user;
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, String.format("Unable to perform execute Query: %s", ex.getMessage()));
        }
    }
    @Override
    public void createUser(User user) throws DataAccessException {
        String SQLCreateUser = "INSERT INTO user (username, password, email) SELECT '"
                + user.username() + "', '" + user.password() + "', '" + user.email() +
                "' WHERE NOT EXISTS ( SELECT 1 FROM user WHERE username = '" + user.username() + "')";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(SQLCreateUser)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, String.format("Unable to perform execute Update: %s", ex.getMessage()));
        }
    }
    @Override
    public void clear(){
        String SQLClearUsers = "DELETE FROM user";
        try {
            DatabaseManager.executeUpdate(SQLClearUsers);
        }
        catch (DataAccessException ex) {
            System.out.println(String.format("Error:", ex.getMessage()));
        }
    }
    private final String[] createUserTable = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(password)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

}
