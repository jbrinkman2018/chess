package client.chessClient;

import client.State;
import dataAccess.DataAccessException;
import model.User;
import server.ServerFacade;

public class LoggedOutClient {
    private ChessClient client;
    private final ServerFacade server;
    LoggedOutClient(ServerFacade server) {
        this.server = server;
    }
    public String eval(String cmd, String[] params, ChessClient client) throws DataAccessException {
        this.client = client;
        return switch(cmd){
            case "register" -> register(params);
            case "login" -> login(params);
            case "quit" -> "quit";
            default -> client.help();
        };
    }
    public String register(String... params) throws DataAccessException {
        if (params.length >= 3) {
            var user = new User(params[0], params[1], params[2]);
            client.setAuthToken(server.register(user).authToken());
            client.setState(State.LOGGEDIN);
            return String.format("You registered with the following info \n username: %s, password: %s, email: %s",
                    user.username(), user.password(), user.email());
        }
        throw new DataAccessException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }
    public String login(String... params) throws DataAccessException {
        if (params.length >= 2) {
            var user = new User(params[0], params[1], null);
            String myAuth = server.login(user).authToken();
            client.setAuthToken(myAuth);
            client.setState(State.LOGGEDIN);
            return String.format("You logged in with the following info \n username: %s, password: %s", user.username(), user.password());
        }
        throw new DataAccessException(400, "Expected: <USERNAME> <PASSWORD>");
    }
}
