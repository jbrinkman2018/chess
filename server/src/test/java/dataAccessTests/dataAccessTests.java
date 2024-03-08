package dataAccessTests;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.authDAOs.*;
import dataAccess.gameDAOs.*;
import dataAccess.userDAOs.*;
import org.junit.jupiter.api.*;
import server.*;
import services.*;
import services.gameServices.*;
import services.userServices.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;

import javax.xml.crypto.Data;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class dataAccessTests {

    @BeforeEach
    public void setup() {
        UserDAO userDAO = new SQLUserDAO();
        AuthDAO authDAO = new SQLAuthDAO();
        GameDAO gameDAO = new SQLGameDAO();
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }
    @AfterEach
    public void cleanup() {
        UserDAO userDAO = new SQLUserDAO();
        AuthDAO authDAO = new SQLAuthDAO();
        GameDAO gameDAO = new SQLGameDAO();
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    @Test
    @DisplayName("create User Positive Unit Test")
    public void createUserPosUnitTest() throws DataAccessException{
        DatabaseManager.getConnection();
        String SQLGetUsers = "SELECT * FROM user";
        var resultUsers = new ArrayList<User>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(SQLGetUsers)) {
                ResultSet rs = preparedStatement.executeQuery();
                User user = null;
                while (rs.next()) {
                    user = new User(rs.getString(1), rs.getString(2), rs.getString(3));
                    resultUsers.add(user);
                }
            }
        } catch (
                SQLException ex) {
            throw new DataAccessException(500, String.format("Unable to perform execute Query: %s", ex.getMessage()));
        }
        UserDAO userDAO = new SQLUserDAO();
        userDAO.createUser(new User("newUser", "newPassword", "newEmail"));
        String SQLGetNewUser = "SELECT * FROM user WHERE username = 'newUser'";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(SQLGetNewUser)) {
                ResultSet rs = preparedStatement.executeQuery();
                User newUser = null;
                if (rs.next()) {
                    newUser = new User(rs.getString(1), rs.getString(2), rs.getString(3));
                }
                assertEquals("newPassword", newUser.password(), "newUser does not have the right password");
            }
        } catch (
                SQLException ex) {
            throw new DataAccessException(500, String.format("Unable to perform execute Query: %s", ex.getMessage()));
        }
    }

    @Test
    @DisplayName("create User Negative Unit Test")
    public void createUserNegUnitTest() throws DataAccessException{
        DatabaseManager.getConnection();
        UserDAO userDAO = new SQLUserDAO();
        userDAO.createUser(new User("newUser", "newPassword", "newEmail"));
        userDAO.createUser(new User("newUser", "wrongPassword", "newEmail"));
        String SQLGetNewUser = "SELECT * FROM user WHERE username = 'newUser'";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(SQLGetNewUser)) {
                ResultSet rs = preparedStatement.executeQuery();
                User newUser = null;
                if (rs.next()) {
                    newUser = new User(rs.getString(1), rs.getString(2), rs.getString(3));
                }
                assertEquals("newPassword", newUser.password(), "newUser does not have the right password");
            }
        } catch (
                SQLException ex) {
            throw new DataAccessException(500, String.format("Unable to perform execute Query: %s", ex.getMessage()));
        }
    }

    @Test
    @DisplayName("Get User Positive Unit Test")
    public void getUserPosUnitTest() throws DataAccessException{
        UserDAO userDAO = new SQLUserDAO();
        userDAO.createUser(new User("newUser", "newPassword", "newEmail"));
        User user = userDAO.getUser("newUser");
        assertEquals("newPassword", user.password(), "got wrong user");
    }

    @Test
    @DisplayName("get User Negative Unit Test")
    public void getUserNegUnitTest() throws DataAccessException{
        UserDAO userDAO = new SQLUserDAO();
        userDAO.createUser(new User("newUser", "newPassword", "newEmail"));
        User user = userDAO.getUser("unknownUser");
        assertNull(user, "created some unknown user when running getUser");
    }

    @Test
    @DisplayName("Clear Users Positive Unit Test")
    public void clearUsersPos() throws DataAccessException{
        UserDAO userDAO = new SQLUserDAO();
        userDAO.clear();
        User user = userDAO.getUser("newUser");
        assertNull(user, "created some unknown user when running getUser");
    }

    @Test
    @DisplayName("create Auth Positvie Unit Test")
    public void createAuthPosUnitTest() throws DataAccessException{
        AuthDAO authDAO = new SQLAuthDAO();
        authDAO.createAuth("newUser");
        String SQLGetNewAuth = "SELECT * FROM auth WHERE username = 'newUser'";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(SQLGetNewAuth)) {
                ResultSet rs = preparedStatement.executeQuery();
                Auth newAuth = null;
                if (rs.next()) {
                    newAuth = new Auth(rs.getString(1), rs.getString(2));
                }
                assertEquals("newUser", newAuth.username(), "new auth Token does not have the right username");
            }
        } catch (
                SQLException ex) {
            throw new DataAccessException(500, String.format("Unable to perform execute Query: %s", ex.getMessage()));
        }
    }
    @Test
    @DisplayName("create Auth Neg  Unit Test")
    public void createAuthNegUnitTest() throws DataAccessException{
        AuthDAO authDAO = new SQLAuthDAO();
        authDAO.createAuth("newUser");
        authDAO.createAuth("newUser");
        String SQLGetNewAuth = "SELECT * FROM auth WHERE username = 'newUser'";
        var auths = new ArrayList<Auth>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(SQLGetNewAuth)) {
                ResultSet rs = preparedStatement.executeQuery();
                Auth newAuth = null;
                while (rs.next()) {
                    newAuth = new Auth(rs.getString(1), rs.getString(2));
                    auths.add(newAuth);
                }
                assertNotEquals(1, auths.size(), "Not a new auth Token every request");
            }
        } catch (
                SQLException ex) {
            throw new DataAccessException(500, String.format("Unable to perform execute Query: %s", ex.getMessage()));
        }
    }

    @Test
    @DisplayName("get Auth positive Unit Test")
    public void getAuthPosUnitTest() throws DataAccessException {
        AuthDAO authDAO = new SQLAuthDAO();
        Auth myAuth = authDAO.createAuth("newUser");
        Auth validAuth = authDAO.getAuth(myAuth.authToken());
        assertEquals("newUser", validAuth.username(), "wrong auth Token");
    }

    @Test
    @DisplayName("get Auth negative Unit Test")
    public void getAuthNegUnitTest() throws DataAccessException{
        AuthDAO authDAO = new SQLAuthDAO();
        Auth myAuth = authDAO.createAuth("newUser");
        Auth anotherAuth = authDAO.createAuth("newUser");
        Auth validAuth = authDAO.getAuth(myAuth.authToken());
        assertNotEquals(anotherAuth.authToken(), validAuth.authToken(), "did not generate distinct auth Tokens");
    }

    @Test
    @DisplayName("delete Auth Positive Unit Test")
    public void deleteAuthPosUnitTest() throws DataAccessException{
        AuthDAO authDAO = new SQLAuthDAO();
        Auth myAuth = authDAO.createAuth("newUser");
        authDAO.deleteAuth(myAuth.authToken());
        Auth gotAuth = authDAO.getAuth(myAuth.authToken());
        assertNull(gotAuth,"did not delete auth");
    }

    @Test
    @DisplayName("delete Auth Negative Unit Test")
    public void deleteAuthNegUnitTest() throws DataAccessException{
        AuthDAO authDAO = new SQLAuthDAO();
        Auth myAuth = authDAO.createAuth("newUser");
        authDAO.deleteAuth("randomAuthToken");
        Auth gotAuth = authDAO.getAuth(myAuth.authToken());
        assertEquals("newUser",myAuth.username(), "did delete auth when shouldn't have");
    }

    @Test
    @DisplayName("get Auth negative Unit Test")
    public void clearAuthPosUnitTest() throws DataAccessException{
        AuthDAO authDAO = new SQLAuthDAO();
        Auth myAuth = authDAO.createAuth("newUser");
        Auth anotherAuth = authDAO.createAuth("newUser");
        Auth lastAuth = authDAO.createAuth("newUser");
        authDAO.clear();
        Auth gotAuth = authDAO.getAuth(lastAuth.authToken());
        assertNull(gotAuth, "did not clear auth tokens");
    }

    @Test
    @DisplayName("create Games Pos Unit Test")
    public void createGamePosUnitTest() throws DataAccessException{
        GameDAO gameDAO = new SQLGameDAO();
        gameDAO.createGame(new Game("newGameName", 0, null, null, null));
        String SQLGetNewGame = "SELECT * FROM game WHERE gameName = 'newGameName'";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(SQLGetNewGame)) {
                ResultSet rs = preparedStatement.executeQuery();
                Game newGame = null;
                if (rs.next()) {
                    var jsonChessGame = rs.getString("chessGame");
                    var chessGame = new Gson().fromJson(jsonChessGame, ChessGame.class);
                    newGame = new Game(rs.getString(1), rs.getInt(2),rs.getString(3), rs.getString(4), chessGame);
                }
                assertEquals("newGameName", newGame.gameName(), "new Game not created");
            }
        } catch (
                SQLException ex) {
            throw new DataAccessException(500, String.format("Unable to perform execute Query: %s", ex.getMessage()));
        }
    }

    @Test
    @DisplayName("create Games Neg Unit Test")
    public void createGamesNegUnitTest() throws DataAccessException{
        GameDAO gameDAO = new SQLGameDAO();
        gameDAO.createGame(new Game("newGameName", 0, null, null, null));
        String SQLGetNewGame = "SELECT * FROM game WHERE gameName = 'unknownGame'";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(SQLGetNewGame)) {
                ResultSet rs = preparedStatement.executeQuery();
                Game newGame = null;
                if (rs.next()) {
                    var jsonChessGame = rs.getString("chessGame");
                    var chessGame = new Gson().fromJson(jsonChessGame, ChessGame.class);
                    newGame = new Game(rs.getString(1), rs.getInt(2),rs.getString(3), rs.getString(4), chessGame);
                }
                assertNull(newGame, "random Game  created");
            }
        } catch (
                SQLException ex) {
            throw new DataAccessException(500, String.format("Unable to perform execute Query: %s", ex.getMessage()));
        }
    }

    @Test
    @DisplayName("list Games Pos Unit Test")
    public void listGamesPosUnitTest(){
        GameDAO gameDAO = new SQLGameDAO();
        gameDAO.createGame(new Game("newGameName", 1, null, null, null));
        gameDAO.createGame(new Game("theBetterGame", 2, null, null, null));
        gameDAO.createGame(new Game("theBestGame", 3, null, null, null));
        var listedGames = gameDAO.listGames();
        var listGames =  listedGames.stream().toArray();
        assertEquals(3, listGames.length,"Not all games were listed");
    }

    @Test
    @DisplayName("list Games Neg Unit Test")
    public void listGamesNegUnitTest(){
        GameDAO gameDAO = new SQLGameDAO();
        gameDAO.createGame(new Game("newGameName", 0, null, null, null));
        gameDAO.createGame(new Game("theBetterGame", 0, null, null, null));
        gameDAO.createGame(new Game("theBestGame", 0, null, null, null));var listedGames = gameDAO.listGames();
        var listGames =  listedGames.stream().toArray();
        assertNotEquals(4, listGames.length,"Not all games were listed");
    }

    @Test
    @DisplayName("get Game Pos Unit Test")
    public void getGamePosUnitTest(){
        GameDAO gameDAO = new SQLGameDAO();
        int gameID = gameDAO.createGame(new Game("newGameName", 1, null, null, null));
        var game =  gameDAO.getGame(gameID);
        assertEquals("newGameName", game.gameName(),"game not imported");
    }

    @Test
    @DisplayName("get Game Neg Unit Test")
    public void getGameNegUnitTest(){
        GameDAO gameDAO = new SQLGameDAO();
        int gameID = gameDAO.createGame(new Game("newGameName", 1, null, null, null));
        var game =  gameDAO.getGame(gameID+1);
        assertNull(game,"game created too high of a gameID");
    }
    @Test
    @DisplayName("update Game Pos Unit Test")
    public void updateGamePosUnitTest() throws DataAccessException{
        GameDAO gameDAO = new SQLGameDAO();
        int gameID = gameDAO.createGame(new Game("newGameName", 1, null, null, null));
        gameDAO.updateGame(gameID, ChessGame.TeamColor.WHITE, "newUser");
        assertEquals("newUser",gameDAO.getGame(gameID).whiteUsername(), "game not updated");
    }

    @Test
    @DisplayName("update Games Neg Unit Test")
    public void updateGameNegUnitTest() throws DataAccessException{
        GameDAO gameDAO = new SQLGameDAO();
        int gameID = gameDAO.createGame(new Game("newGameName", 1, null, null, null));
        gameDAO.updateGame(gameID, ChessGame.TeamColor.BLACK, "newUser");
        assertEquals("null",gameDAO.getGame(gameID).whiteUsername(), "game updated wrong color");
    }

    @Test
    @DisplayName("clear Games Pos Unit Test")
    public void clearGamesPosUnitTest(){
        GameDAO gameDAO = new SQLGameDAO();
        int gameID = gameDAO.createGame(new Game("newGameName", 1, null, null, null));
        gameDAO.clear();
        assertNull(gameDAO.getGame(gameID), "games not cleared");
    }
}
