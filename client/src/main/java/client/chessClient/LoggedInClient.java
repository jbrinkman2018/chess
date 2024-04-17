package client.chessClient;

import chess.ChessGame;
import client.State;
import client.webSocket.GameHandler;
import client.webSocket.WebSocketFacade;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.Game;
import model.JoinRequest;
import server.ServerFacade;

public class LoggedInClient implements GameHandler {
    private ChessGame.TeamColor playerColor;
    private ChessClient client;
    private final ServerFacade server;
    private GamePlay gameplay;
    WebSocketFacade wsFacade;
    private int gameID;
    private ChessGame myChessGame;
    LoggedInClient(ServerFacade server) {

        this.server = server;
    }
    public String eval(String cmd, String[] params, ChessClient client) throws DataAccessException {
        this.client = client;
        return switch(cmd){
            case "logout" -> logout();
            case "list" -> listGames();
            case "create" -> createGame(params);
            case "join" -> joinGame(params);
            case "observe" -> observeGame(params);
            case "redraw" -> redrawBoard();
            case "leave" -> leaveGame();
            case "move" -> makeMove(params);
            case "highlight" -> showMoves(params);
            case "resign" -> resign();
            case "quit" -> "quit";
            default -> client.help();
        };
    }
    public String logout() throws DataAccessException {
        server.logout(client.getAuthToken());
        client.setState(State.LOGGEDOUT);
        return "You logged out";
    }
    public String listGames() throws DataAccessException {
        assertLoggedIn();
        var games = server.listGames(client.getAuthToken());
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
            var response = server.createGame(game, client.getAuthToken());
            return String.format("You created a game, here is your game ID: %d", response.gameID());
        }
        throw new DataAccessException(400, "Expected: <GAMENAME>");
    }
    public String joinGame(String... params) throws DataAccessException {
        assertLoggedIn();
        if (params.length >= 2) {
            this.gameID = Integer.parseInt(params[0]);
            var inputColor = params[1];
            Game myGame = null;
            if (inputColor.toLowerCase().equals("WHITE".toLowerCase())){
                this.playerColor = ChessGame.TeamColor.WHITE;
            }
            else if (inputColor.toLowerCase().equals("BLACK".toLowerCase())){
                this.playerColor = ChessGame.TeamColor.BLACK;
            }
            else {
                throw new DataAccessException(400, "Expected: <GAMEID> [WHITE|BLACK]");
            }
            JoinRequest joinRequest= new JoinRequest(playerColor, gameID);
            server.joinGame(joinRequest, client.getAuthToken());
            wsFacade = new WebSocketFacade(client.getServerUrl(), this);
            wsFacade.joinPlayer(gameID, client.getAuthToken(), playerColor);
            return "";
        }
        throw new DataAccessException(400, "Expected: <GAMEID> [WHITE|BLACK]");
    }
    public String observeGame(String... params) throws DataAccessException {
        assertLoggedIn();
        this.playerColor = null;
        if (params.length >= 1) {
            var gameID = Integer.parseInt(params[0]);
            JoinRequest joinRequest = new JoinRequest(null, gameID);
            var response = server.joinGame(joinRequest, client.getAuthToken());
            wsFacade = new WebSocketFacade(client.getServerUrl(), this);
            wsFacade.joinObserver(gameID, client.getAuthToken());
            return "";
        } else {
            throw new DataAccessException(400, "Expected: <GAMEID>");
        }
    }
    private String redrawBoard() throws DataAccessException{
        assertGamePlay();
        return gameplay.redrawBoard();
    }
    private String leaveGame() throws DataAccessException{
        assertGamePlay();
        wsFacade.leaveGame(this.gameID, client.getAuthToken());
        this.wsFacade = null;
        client.setState(State.LOGGEDIN);
        return "";
    }
    private String makeMove(String... params) throws DataAccessException{
        assertGamePlay();
        var chessMove = gameplay.makeMove(params);
        wsFacade.makeMove(gameID, client.getAuthToken(), chessMove);
        return redrawBoard();
    }
    private String showMoves(String... params) throws DataAccessException{
        assertGamePlay();
        return gameplay.showMoves(params);
    }
    private String resign() throws DataAccessException{
        assertGamePlay();
        wsFacade.resignGame(this.gameID, client.getAuthToken());
        return gameplay.resign();
    }
    private void assertLoggedIn() throws DataAccessException {
        if (client.getState() != State.LOGGEDIN) {
            if (client.getState() == State.GAMEPLAY){
                throw new DataAccessException(400, "You are already in a game. Please Leave first");
            }
            else {
                throw new DataAccessException(400, "You must sign in");
            }
        }
    }
    private void assertGamePlay() throws DataAccessException {
        if (client.getState() != State.GAMEPLAY) {
            throw new DataAccessException(400, "You must be in a game");
        }
    }
    @Override
    public void updateGame(ChessGame game){
        if (gameplay == null){
            gameplay = new GamePlay(this.playerColor, game);
        } else {
            gameplay.game = game;
        }
        client.setState(State.GAMEPLAY);
        System.out.println(gameplay.redrawBoard());
    }
    @Override
    public void printMessage(String message){
        System.out.println(message);
    }
}
