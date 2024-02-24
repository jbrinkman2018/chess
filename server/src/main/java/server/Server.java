package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import spark.*;
import model.*;
import services.*;

import java.util.zip.DataFormatException;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
//        Spark.init();

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registration);

//        Spark.post("/session", this::login);
//        Spark.delete("/session", this::logout);
//        Spark.get("/game", this::listGames);
//        Spark.post("/game", this::createGame);
//        Spark.put("/game", this::joinGame);
//        Spark.delete("/db", this::clear);\
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
        var service = new RegisterService();
        String authToken = service.register(user);
        return new Gson().toJson(authToken);
    }
    private void exceptionHandler(DataAccessException ex, Request req, Response res){
        res.status(ex.getStatusCode());
        res.body(ex.getMessage());
    }
//    private Object filter (Request req, Response res){
//        System.out.println("yipee");
//        return "empty";
//    }
//    private Object login (Request req, Response res) {
//        return
//    }
//    private Object logout (Request req, Response res) {
//        return
//    }

//    private Object listGames(Request req, Response res) {
//        return
//    }

//    private Object createGame(Request req, Response res) {
//        return
//    }

//    private Object joinGame(Request req, Response res) {
//        return
//    }

//    private clear(Request req, Response res) {
//        return
//    }

}
