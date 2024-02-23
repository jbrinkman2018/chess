package server;

import spark.*;
import com.google.gson.Gson;
import model.*;
import services.*;
import dataAccess.*;

public class Server {
    private services.Service service;
    public Server() {}

    public int run(int desiredPort) {
        Spark.port(desiredPort);

//        Spark.staticFiles.location("/web");
        Spark.externalStaticFileLocation("/Users/jaredbrinkman/Documents/web");
        Spark.before(this::filter);
//        Spark.notFound("<html><body>My custom 404 page</body></html>");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registration);

//        Spark.post("/session", this::login);
//        Spark.delete("/session", this::logout);
//        Spark.get("/game", this::listGames);
//        Spark.post("/game", this::createGame);
//        Spark.put("/game", this::joinGame);
//        Spark.delete("/db", this::clear);\
//        Spark.exception(Response.class, this::exceptionHandler);
        Spark.notFound("<html><body>My custom 404 page</body></html>");

//        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


    private Object registration (Request req, Response res) {
        var user = new Gson().fromJson(req.body(), User.class);
        service = new RegisterService();
        String authToken = service.register(user);
        return new Gson().toJson(authToken);
    }
    private Object filter (Request req, Response res){
        System.out.println("yipee");
        return "empty";
    }
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
