package client.chessClient;

import client.*;
import DataAccessException.DataAccessException;
import server.ServerFacade;

import java.util.Arrays;

public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGEDOUT;
    private String myAuthToken = null;
    private GamePlay gameplay = null;
    private LoggedOutClient loClient;
    private LoggedInClient liClient;
    public ChessClient(String serverUrl){
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
        loClient = new LoggedOutClient(server);
        liClient = new LoggedInClient(server);
    }
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (state) {
                case LOGGEDOUT -> loClient.eval(cmd, params, this);
                case LOGGEDIN -> liClient.eval(cmd, params, this);
                case GAMEPLAY -> liClient.eval(cmd, params, this);
            };
        } catch (DataAccessException ex) {
        return ex.getMessage();
        }
    }
    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    - login <USERNAME> <PASSWORD>
                    - register <USERNAME> <PASSWORD> <EMAIL>
                    - quit";
                    """;
        }else if (state == State.LOGGEDIN) {
            return """
                - create <GAMENAME> - a game
                - list - games
                - join <ID> [WHITE|BLACK] - a game
                - observe <GAMEID> - a game
                - logout
                - quit
                - help - with possible commands";
                """;
        } else return
                """
                In gameplay:
                - redraw - redraw the chessboard
                - leave - leave the game
                - move <POSITION><MOVE>
                - resign - resign the game
                - highlight <Position> - highlight legal moves
                              for piece in this position on the board
                - quit
                - help - with possible commands";
                """;
    }
    public void setState(State state) {
        this.state = state;
    }
    public State getState(){
        return this.state;
    }
    public void setAuthToken(String authToken){
        this.myAuthToken = authToken;
    }
    public String getAuthToken(){
        return this.myAuthToken;
    }
    public String getServerUrl(){
        return serverUrl;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }
}