package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.authDAOs.AuthDAO;
import dataAccess.authDAOs.MemoryAuthDAO;
import dataAccess.gameDAOs.GameDAO;
import dataAccess.gameDAOs.MemoryGameDAO;
import dataAccess.userDAOs.MemoryUserDAO;
import dataAccess.userDAOs.UserDAO;
import services.gameServices.*;
import services.*;
import services.userServices.LoginService;
import services.userServices.LogoutService;
import services.userServices.RegisterService;
import spark.*;
import model.*;
import spark.Response;

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
//        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
    private Object registration (Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), User.class);
        var service = new RegisterService(userDAO, authDAO);
        authOutput = service.register(user);
        return new Gson().toJson(authOutput);
    }
    private Object login (Request req, Response res) throws DataAccessException{
        var user = new Gson().fromJson(req.body(), User.class);
        var service = new LoginService(userDAO, authDAO);
        authOutput = service.login(user);
        return new Gson().toJson(authOutput);
    }
    private Object logout (Request req, Response res) throws DataAccessException{
        var auth = new Gson().fromJson(req.headers("Authorization"), Auth.class);
        var service = new LogoutService(userDAO, authDAO);
        service.logout(auth);
        res.status(200);
        return new Gson().toJson("{}");
//        return "{}";
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        var auth = new Gson().fromJson(req.headers("Authorization"), Auth.class); // change req.body to req.headers
        var service = new ListGamesService(gameDAO, authDAO);
        service.verifyAuth(auth);
        return new Gson().toJson(service.listGames().toArray());
    }

    private Object createGame(Request req, Response res) throws DataAccessException{
        var game = new Gson().fromJson(req.body(), Game.class);
        var auth = new Gson().fromJson(req.headers("Authorization"), Auth.class); // change req.body to req.headers
        var service = new CreateGameService(gameDAO, authDAO);
        service.verifyAuth(auth);
        return new Gson().toJson(service.createGame(game));
    }

//    private Object joinGame(Request req, Response res) {
//        return
//    }

    private Object clear(Request req, Response res) {
        var service = new ClearService(userDAO, authDAO, gameDAO);
        service.clear();
        res.status(200);
        return "";
    }
    private void exceptionHandler(DataAccessException ex, Request req, Response res){
        res.halt(ex.getStatusCode(), ex.getMessage());
//        res.status(ex.getStatusCode());
//        StringBuilder errorMessage = new StringBuilder();
//        errorMessage.append("message : ");
//        errorMessage.append(ex.getMessage());
//        res.body(new Gson().toJson(ex.getStatusCode()));
    }
}
