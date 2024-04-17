package clientTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import model.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {


    private static Server server;
    static ServerFacade facade;
    @BeforeAll
    public static void init() throws DataAccessException {
        server = new Server();
        var port = server.run(8081);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
        facade.clear();
    }
    @AfterAll
    static void stopServer() {
        server.stop();
    }
    @AfterEach
    void clear() throws DataAccessException{
        facade.clear();
    }
    @Test
    void registerPositive() throws Exception {
        var authData = facade.register(new User("player1", "password", "p1@email.com"));
        assertTrue(authData.authToken().length() > 10);
    }
    @Test
    void registerNegative() throws Exception {
        var authData = facade.register(new User("player1", "password", "p1@email.com"));
        try {
            var authDataCopy = facade.register(new User("player1", "password", "p1@email.com"));
            assertFalse(authDataCopy.authToken().length() > 10);
        }
        catch (DataAccessException ex){
            if (ex.getStatusCode() == 500) {
                assertEquals(ex.getMessage(), "failure: 403");
            }
            else {
                var condition = false;
                assertTrue(condition);
            }
        }
    }
    @Test
    void loginPositive() throws Exception {
        var authData = facade.register(new User("player1", "password", "email@email.com"));
        var authDataLogin = facade.login(new User("player1", "password", null));
        assertTrue(authDataLogin.authToken().length() > 10);
    }
    @Test
    void loginNegative() throws Exception {
        var authData = facade.register(new User("player1", "password", "email@email.com"));
        try {
            var authDataLogin = facade.login(new User("playerEmpty", "password", null));
            assertFalse(authDataLogin.authToken().length() > 10);
        }
        catch (DataAccessException ex){
            if (ex.getStatusCode() == 500) {
                assertEquals(ex.getMessage(), "failure: 401");
            }
            else {
                var condition = false;
                assertTrue(condition);
            }
        }
    }
    @Test
    void logoutPositive() throws Exception {
        var authData = facade.register(new User("player1", "password", "p1@email.com"));
        try {
            facade.logout(authData.authToken());
            var loggedout = true;
            assertTrue(loggedout);
        }
        catch(DataAccessException ex) {
            var DataAccessError = true;
            assertFalse(DataAccessError);
        }
    }
    @Test
    void logoutNegative() throws Exception {
        facade.clear();
        var authToken = "randomauthToken";
        try {
            facade.logout(authToken);
            var loggedout = true;
            assertFalse(loggedout);
        }
        catch (DataAccessException ex) {
            if (ex.getStatusCode() == 500) {
                assertEquals("failure: 401", ex.getMessage());
            }
        }
    }
    @Test
    void createGamePositive() throws Exception {
        var authData = facade.register(new User("player1", "password", "email@email.com"));
        var gameData = facade.createGame(new Game("newGame", 1, null, null, null), authData.authToken());
        assertEquals(gameData.gameID(), 1);
    }
    @Test
    void createGameNegative() throws Exception {
        var authData = facade.register(new User("player1", "password", "email@email.com"));
        var gameData = facade.createGame(new Game("newGame", 1, null, null, null), authData.authToken());
        var gameDataCopy = facade.createGame(new Game("newGame", 1, null, null, null), authData.authToken());
        assertNotEquals(gameDataCopy.gameID(), 1);
    }
    @Test
    void listGamesPositive() throws Exception {
        var authData = facade.register(new User("player1", "password", "email@email.com"));
        var gameDataOne = facade.createGame(new Game("newGameOne", 1, null, null, null), authData.authToken());
        var gameDataTwo = facade.createGame(new Game("newGameTwo", 2, null, null, null), authData.authToken());
        var listedGames = facade.listGames(authData.authToken());
        assertTrue(listedGames.length > 1);
    }
    @Test
    void listGamesNegative() throws Exception {
        var authToken = "randomAuthToken";
        try{
            var gameDataOne = facade.createGame(new Game("newGameOne", 1, null, null, null), authToken);
            var gameDataTwo = facade.createGame(new Game("newGameTwo", 2, null, null, null), authToken);
            var listedGames = facade.listGames(authToken);
            assertFalse(listedGames.length > 1);
        }
        catch (DataAccessException ex) {
            if (ex.getStatusCode() == 500) {
                assertEquals("failure: 401", ex.getMessage());
            }
        }
    }
    @Test
    void joinGamePositive() throws Exception {
        var authData = facade.register(new User("player1", "password", "email@email.com"));
        var gameDataOne = facade.createGame(new Game("newGameOne", 1, null, null, null), authData.authToken());
        var joinRequest = new JoinRequest(ChessGame.TeamColor.WHITE, 1);
        var successResponse = facade.joinGame(joinRequest, authData.authToken());
        var listedGames = facade.listGames(authData.authToken());
        assertEquals(authData.username(), listedGames[0].whiteUsername());
    }
    @Test
    void joinGameNegative() throws Exception {
        var authData = facade.register(new User("player1", "password", "email@email.com"));
        var gameDataOne = facade.createGame(new Game("newGameOne", 1, null, null, null), authData.authToken());
        try{

            var successResponse = facade.joinGame(new JoinRequest(ChessGame.TeamColor.WHITE, 1), authData.authToken());
            var failedResponse = facade.joinGame(new JoinRequest(ChessGame.TeamColor.WHITE, 1), authData.authToken());
        }
        catch (DataAccessException ex) {
            var dataAccessException = true;
            assertTrue(dataAccessException);
        }
    }
}