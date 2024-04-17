package dataAccess.authDAOs;
import DataAccessException.DataAccessException;
import dataAccess.DatabaseManager;
import model.*;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {
    public SQLAuthDAO(){
        try {
            DatabaseManager.configTable(createAuthTable);
        }
        catch(DataAccessException ex) {
            System.out.println(ex.getStatusCode() + String.format(", \"message\" Error: %s", ex.getMessage()));
        }
    }

    @Override
    public Auth createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        Auth myAuth = new Auth(username, authToken);
        String SQLCreateAuth = "INSERT INTO auth (username, authToken) VALUES ('"
                + myAuth.username() + "', '" + myAuth.authToken() + "')";
        DatabaseManager.executeUpdate(SQLCreateAuth);
        return myAuth;
    }
    @Override
    public void deleteAuth(String authToken){
        String SQLClearAuths = "DELETE FROM auth WHERE authToken = '" + authToken + "'";
        try {
            DatabaseManager.executeUpdate(SQLClearAuths);
        }
        catch (DataAccessException ex) {
            System.out.println(String.format("Error:", ex.getMessage()));
        }
    }
    @Override
    public Auth getAuth(String authToken) {
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var statement = "SELECT username, authToken FROM auth WHERE authToken = '" + authToken + "'";
                try (var ps = conn.prepareStatement(statement)) {
                    try (var rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return readAuth(rs);
                        }
                    }
                }
            } catch (Exception e) {
                throw new DataAccessException(500, String.format("Unable to read data: %s", e.getMessage()));
            }
        }catch (DataAccessException ex) {
            System.out.println(String.format("Error:", ex.getMessage()));
        }
        return null;
    }
    @Override
    public void clear(){
        String SQLClearAuths = "DELETE FROM auth";
        try {
            DatabaseManager.executeUpdate(SQLClearAuths);
        }
        catch (DataAccessException ex) {
            System.out.println(String.format("Error:", ex.getMessage()));
        }
    }
    private final String[] createAuthTable = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `username` varchar(256) NOT NULL,
              `authToken` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private Auth readAuth(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var authToken = rs.getString("authToken");
        return new Auth(username, authToken);
    }
}
