package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.authDAOs.*;
import dataAccess.gameDAOs.*;
import dataAccess.userDAOs.*;
import services.gameServices.*;
import services.*;
import services.userServices.*;
import spark.*;
import model.*;
import spark.Response;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collection;

public class Server {
    public Server() {}
    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();
    private Auth authOutput;
//    public Server(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
//        this.userDAO = userDAO;
//        this.authDAO = authDAO;
//        this.gameDAO = gameDAO;
//    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registration);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
    private Object registration (Request req, Response res) throws DataAccessException{
        var user = new Gson().fromJson(req.body(), User.class);
        var service = new RegisterService(userDAO, authDAO);
        authOutput = service.register(user);
        res.type("application/json");
        return new Gson().toJson(authOutput);
    }
    private Object login (Request req, Response res) throws DataAccessException{
        var user = new Gson().fromJson(req.body(), User.class);
        var service = new LoginService(userDAO, authDAO);
        authOutput = service.login(user);
        res.type("application/json");
        return new Gson().toJson(authOutput);
    }
    private Object logout (Request req, Response res) throws DataAccessException{
        var auth = req.headers("Authorization");
        var service = new LogoutService(userDAO, authDAO);
        service.logout(auth);
        res.status(200);
        res.type("application/json");
        return "{}";
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        var auth = req.headers("Authorization");
        var service = new ListGamesService(gameDAO, authDAO);
        service.verifyAuth(auth);
        res.type("application/json");
        var list = service.listGames().toArray();
        return new Gson().toJson(Map.of("games", list));
    }

    private Object createGame(Request req, Response res) throws DataAccessException{
        var auth = req.headers("Authorization");
        var game = new Gson().fromJson(req.body(), Game.class);
        var service = new CreateGameService(gameDAO, authDAO);
        service.verifyAuth(auth);
        res.type("application/json");
        return new Gson().toJson(Map.of("gameID", service.createGame(game)));
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        var auth = req.headers("Authorization");
        var playerJoinGame = new Gson().fromJson(req.body(),JoinGameInfo.class);
        var service = new JoinGameService(gameDAO, authDAO);
        service.verifyAuth(auth);
        service.joinGame(playerJoinGame.getGameID(), playerJoinGame.getPlayerColor());
        res.status(200);
        return "{}";
    }

    private Object clear(Request req, Response res) {
        var service = new ClearService(userDAO, authDAO, gameDAO);
        service.clear();
        res.status(200);
        res.type("application/json");
        return "{}";
    }
    private Object exceptionHandler(DataAccessException ex, Request req, Response res){
//        res.halt(ex.getStatusCode(), ex.getMessage());
        res.status(ex.getStatusCode());
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", ex.getMessage())));
        res.body(body);
        return new Gson().toJson(body);
    }
}
