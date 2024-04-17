import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.authDAOs.*;
import dataAccess.gameDAOs.*;
import dataAccess.userDAOs.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.*;
import services.gameServices.*;
import services.userServices.*;
import static org.junit.jupiter.api.Assertions.*;
import model.*;

public class ServiceTests {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
    }

    @Test
    @DisplayName("register Positive Unit Test")
    public void regPosUnitTest(){
        try {
            var service = new RegisterService(userDAO, authDAO);
            String username = "username";
            String password = "password";
            String email = "email";
            User user = new User(username, password, email);
            service.register(user);
            assertEquals(userDAO.getUser("username"), user, "user was not registered");
        }
        catch(DataAccessException ex){
            System.out.println("register unit test didn't make it to assertequals");
        }
    }

    @Test
    @DisplayName("register Negative Unit Test")
    public void regNegUnitTest(){
        try {
            var service = new RegisterService(userDAO, authDAO);
            String username = "username";
            String password = "password";
            String email = "email";
            User user = new User(username, password, email);
            service.register(user);
            User secondUser = new User(username, "newPassword", email);
            try {
                service.register(secondUser);
            }
            catch(DataAccessException exception) {
                assertNotEquals(userDAO.getUser("username"), secondUser, "second user was registered when they shouldn't have been");
            }
        }
        catch(DataAccessException ex){
            System.out.println("didn't reach asser statement");
        }
    }

    @Test
    @DisplayName("login Positive Unit Test")
    public void loginNegUnitTest(){
        try {
            var service = new LoginService(userDAO, authDAO);
            String username = "username";
            String password = "password";
            String email = "email";
            User user = new User(username, password, email);
            userDAO.createUser(user);
            Auth auth = authDAO.createAuth(username);
            assertNotEquals(auth, service.login(user), "not a unique auth token per login");
        }
        catch(DataAccessException ex){
            System.out.println("login unit test didn't make it to assert equals");
        }
    }

    @Test
    @DisplayName("login Negative Unit Test")
    public void loginNPosUnitTest(){
        try {
            var service = new LoginService(userDAO, authDAO);
            String username = "username";
            String password = "password";
            String email = "email";
            User user = new User(username, password, email);
            userDAO.createUser(user);
            Auth auth = service.login(user);
            assertEquals(auth, authDAO.getAuth(auth.authToken()) , "auth not put in memory");
        }
        catch(DataAccessException ex){
            System.out.println("login unit test didn't make it to assert equals");
        }
    }

    @Test
    @DisplayName("logout Positive Unit Test")
    public void logoutPosUnitTest(){
        try {
            var service = new LogoutService(userDAO, authDAO);
            String username = "username";
            String password = "password";
            String email = "email";
            User user = new User(username, password, email);
            userDAO.createUser(user);
            Auth auth = authDAO.createAuth(user.username());
            service.logout(auth.authToken());
            assertNotEquals(auth, authDAO.getAuth(auth.authToken()) , "auth not deleted from memory");
        }
        catch(DataAccessException ex){
            System.out.println("login unit test didn't make it to assert equals");
        }
    }

    @Test
    @DisplayName("logout Negative Unit Test")
    public void logoutNegUnitTest(){
        try {
            var service = new LogoutService(userDAO, authDAO);
            String username = "username";
            String password = "password";
            String email = "email";
            User user = new User(username, password, email);
            userDAO.createUser(user);
            Auth auth = authDAO.createAuth(user.username());
            try{
                service.verifyAuth("wrongAuth");
            }
            catch(DataAccessException ex_2){
                assertEquals(auth, authDAO.getAuth(auth.authToken()) , "auth not put in memory");
            }

        }
        catch(DataAccessException ex){
            System.out.println("login unit test didn't make it to assert equals");
        }
    }

    @Test
    @DisplayName("list Games Positive Unit Test")
    public void listPosUnitTest(){
        try {
            var service = new ListGamesService(gameDAO, authDAO);
            String gameName = "game";
            Game game = new Game(gameName, 1, "null", "null", null);
            gameDAO.createGame(game);
            assertEquals(service.listGames().toString(), "[" +game.toString() + "]", "game not listed");
        }
        catch(DataAccessException ex){
            System.out.println("login unit test didn't make it to assert equals");
        }
    }

    @Test
    @DisplayName("list Game Negative Unit Test")
    public void listNegUnitTest(){
        try {
            var service = new ListGamesService(gameDAO, authDAO);
            String gameName = "game";
            Game game = new Game(gameName, 1, "null", "null", null);
            gameDAO.createGame(game);
            Auth auth = authDAO.createAuth("userName");
            try {
                service.verifyAuth(auth.authToken());
            }
            catch (DataAccessException exception) {
                assertNotEquals(service.listGames().toString(), "[" +game.toString() + "]", "game not listed");
            }
        }
        catch(DataAccessException ex){
            System.out.println("login unit test didn't make it to assert equals");
        }
    }

    @Test
    @DisplayName("create Game Positive Unit Test")
    public void createPosUnitTest(){
        try {
            var service = new CreateGameService(gameDAO, authDAO);
            String gameName = "game";
            Game game = new Game(gameName, 1, "null", "null", null);
            Auth auth = authDAO.createAuth("userName");
            service.verifyAuth(auth.authToken());
            int gameID = service.createGame(game);
            assertEquals(gameID, gameDAO.getGame(gameID).gameID(), "game not saved");
        }
        catch(DataAccessException ex){
            System.out.println("login unit test didn't make it to assert equals");
        }
    }

    @Test
    @DisplayName("create Game Negative Unit Test")
    public void createNegUnitTest() throws DataAccessException{
        var service = new CreateGameService(gameDAO, authDAO);
        String gameName = "game";
        Game game = new Game(gameName, 1, "null", "null", null);
        Auth auth = authDAO.createAuth("userName");
        int gameID = 200;
        try {
            service.verifyAuth(auth.authToken());
            gameID = service.createGame(game);
        }
        catch (DataAccessException exception){
            assertNotEquals(gameID, gameDAO.getGame(gameID).gameID(), "unauthorization failed");
        }
    }

    @Test
    @DisplayName("join Game Positive Unit Test")
    public void joinPosUnitTest(){
        try {
            var service = new JoinGameService(gameDAO, authDAO);
            String gameName = "game";
            Game game = new Game(gameName, 1, "null", "null", null);
            Auth auth = authDAO.createAuth("userName");
            gameDAO.createGame(game);
            int gameID = 1;
            service.joinGame(gameID, ChessGame.TeamColor.WHITE, "white");
            assertEquals("white", gameDAO.getGame(gameID).whiteUsername(), "didn't add username");
        }
        catch (DataAccessException ex){
            System.out.println("Didn't make it to test cases");
        }
    }

    @Test
    @DisplayName("join Game Negative Unit Test")
    public void joinNegUnitTest() throws DataAccessException {
        var service = new JoinGameService(gameDAO, authDAO);
        String gameName = "game";
        Game game = new Game(gameName, 1, "null", "null", null);
        Auth auth = authDAO.createAuth("userName");
        gameDAO.createGame(game);
        int gameID = 1;
        try {
            service.verifyAuth(auth.authToken());
            service.joinGame(gameID, ChessGame.TeamColor.WHITE, "white");
        }
        catch (DataAccessException exception){
            assertNotEquals("white", gameDAO.getGame(gameID).whiteUsername(), "unauthorization failed");
        }
    }

    @Test
    @DisplayName("clear Unit Test")
    public void clearUnitTest() throws DataAccessException{
        var service = new ClearService(userDAO, authDAO, gameDAO);
        String gameName = "game";
        Game game = new Game(gameName, 1, "null", "null", null);
        gameDAO.createGame(game);
        Auth auth = authDAO.createAuth("userName");
        service.clear();
        assertNull(authDAO.getAuth(auth.authToken()));
    }
}
