package client;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import server.ServerFacade;
import model.*;
import chess.*;
import chess.ChessGame;

import java.util.Arrays;

public class ChessClient {
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    public State state = State.LOGGEDOUT;
    private String myAuthToken = null;
    private GamePlay gameplay = null;
    public ChessClient(String serverUrl){
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
    }
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "list" -> listGames();
                case "create" -> createGame(params);
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "redraw" -> redrawBoard();
                case "leave" -> leaveGame();
                case "move" -> makeMove(params);
                case "showMoves" -> showMoves(params);
                case "resign" -> resign();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws DataAccessException {
        if (params.length >= 3) {
            var user = new User(params[0], params[1], params[2]);
            myAuthToken = server.register(user).authToken();
            state = State.LOGGEDIN;
            return String.format("You registered with the following info \n username: %s, password: %s, email: %s",
                    user.username(), user.password(), user.email());
        }
        throw new DataAccessException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }
    public String login(String... params) throws DataAccessException {
        if (params.length >= 2) {
            var user = new User(params[0], params[1], null);
            state = State.LOGGEDIN;
            myAuthToken = server.login(user).authToken();
            return String.format("You logged in with the following info \n username: %s, password: %s", user.username(), user.password());
        }
        throw new DataAccessException(400, "Expected: <USERNAME> <PASSWORD>");
    }
    public String logout() throws DataAccessException {
        assertLoggedIn();
        server.logout(myAuthToken);
        state = State.LOGGEDOUT;
        return "You logged out";
    }
    public String listGames() throws DataAccessException {
        assertLoggedIn();
        var games = server.listGames(myAuthToken);
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games) {
            result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();
    }

    public String createGame(String... params) throws DataAccessException {
        assertLoggedIn();
        if (params.length >= 1) {
            var gameName = params[0];
            var game = new Game(gameName, 0, null,null,null);
            var response = server.createGame(game, myAuthToken);
            return String.format("You created a game, here is your game ID: %d", response.gameID());
        }
        throw new DataAccessException(400, "Expected: <GAMENAME>");
    }
    public String joinGame(String... params) throws DataAccessException {
        assertLoggedIn();
        if (params.length >= 2) {
            var gameID = Integer.parseInt(params[0]);
            var inputColor = params[1];
            ChessGame.TeamColor playerColor = null;
            if (inputColor.toLowerCase().equals("WHITE".toLowerCase())){
                playerColor = ChessGame.TeamColor.WHITE;
            }
            else if (inputColor.toLowerCase().equals("BLACK".toLowerCase())){
                playerColor = ChessGame.TeamColor.BLACK;
            }
            else {
                throw new DataAccessException(400, "Expected: <GAMEID> [WHITE|BLACK]");
            }
            var game = new Game(null, gameID, playerColor.toString(),null,null);
            var response = server.joinGame(game, myAuthToken);
            state = State.GAMEPLAY;
            if (game.game() == null){
                gameplay = new GamePlay(playerColor, null);
                return gameplay.redrawBoard();
            }
            else {
                gameplay = new GamePlay(playerColor, game.game().getBoard());
                return gameplay.redrawBoard();
            }
        }
        throw new DataAccessException(400, "Expected: <GAMEID> [WHITE|BLACK]");
    }
    public String observeGame(String... params) throws DataAccessException {
        assertLoggedIn();
        if (params.length >= 1) {
            var gameID = Integer.parseInt(params[0]);
            var game = new Game(null, gameID, null, null, null);
            var response = server.joinGame(game, myAuthToken);
            state = State.GAMEPLAY;
            if (game.game() == null){
                gameplay = new GamePlay(null, null);
                return gameplay.redrawBoard();
            }
            else {
                gameplay = new GamePlay(null, game.game().getBoard());
                return gameplay.redrawBoard();
            }
        }k else {
            throw new DataAccessException(400, "Expected: <GAMEID>");
        }
    }
    private String redrawBoard() throws DataAccessException{
        assertGamePlay();
        return gameplay.redrawBoard();
    }
    private String leaveGame() throws DataAccessException{
        assertGamePlay();
        state = State.LOGGEDIN;
        return "You left the game";
    }
    private String makeMove(String... params) throws DataAccessException{
        assertGamePlay();
        return gameplay.makeMove(params);
    }
    private String showMoves(String... params) throws DataAccessException{
        assertGamePlay();
        return gameplay.showMoves(params);
    }
    private String resign() throws DataAccessException{
        assertGamePlay();
        return gameplay.resign();
    }

    private void assertLoggedIn() throws DataAccessException {
        if (state == State.LOGGEDOUT) {
            throw new DataAccessException(400, "You must sign in");
        }
    }
    private void assertGamePlay() throws DataAccessException {
        if (state != State.GAMEPLAY) {
            throw new DataAccessException(400, "You must be in a game");
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
                - move <PIECE><MOVE>
                - resign - resign the game
                - showMoves - highlight legal moves
                - quit
                - help - with possible commands";
                """;
    }
}