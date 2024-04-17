package server.webSocket;

import chess.InvalidMoveException;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.authDAOs.AuthDAO;
import dataAccess.gameDAOs.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.*;
import java.io.IOException;
import java.util.ArrayList;

import server.Server;
import services.gameServices.GameService;
import webSocketMessages.serverMessages.*;
import chess.ChessGame;

import javax.xml.crypto.Data;

public class WebSocketServices extends GameService {
    private final WebSocketSessions sessions;

    public WebSocketServices(GameDAO gameDAO, AuthDAO authDAO) {
        super(gameDAO, authDAO);
        sessions = new WebSocketSessions();
    }

    public void joinPlayer(int gameID, String authToken, ChessGame.TeamColor playerColor, Session session) throws DataAccessException {
        try {
            String username = getUsername(authToken);
            if ((gameDAO.getGame(gameID).whiteUsername() != null) && (playerColor == ChessGame.TeamColor.WHITE)) {
                if (!username.equals(gameDAO.getGame(gameID).whiteUsername())) {
                    throw new DataAccessException(403, "already taken");
                }
            }
            if ((gameDAO.getGame(gameID).blackUsername() != null) && (playerColor == ChessGame.TeamColor.BLACK)) {
                if (!username.equals(gameDAO.getGame(gameID).blackUsername())) {
                    throw new DataAccessException(403, "already taken");
                }
            }
            if ((gameDAO.getGame(gameID).blackUsername() == null) && (gameDAO.getGame(gameID).whiteUsername() == null)){
                throw new DataAccessException(500, "joinPlayer Server Endpoint not working");
            }
            sessions.addSessionToGame(gameID, authToken, session);
            ServerMessage broadcastMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            broadcastMessage.setNotification(String.format("%s joined the game as %s", username, playerColor));
            broadcastMessage(gameID, broadcastMessage, authToken);
            ServerMessage soloMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            var myGame = gameDAO.getGame(gameID);
            if (myGame.game() == null) {
                gameDAO.createNewPlayableGame(myGame);
                myGame = gameDAO.getGame(gameID);
            }
            soloMessage.setGame(myGame.game());
            sendMessage(gameID, soloMessage, authToken);
        } catch (IOException e) {
            throw new DataAccessException(500, e.getMessage());
        }
    }
    public void joinObserver(int gameID, String authToken, Session session) throws DataAccessException{
        try {
            String username = getUsername(authToken);
            sessions.addSessionToGame(gameID, authToken, session);
            ServerMessage broadcastMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            broadcastMessage.setNotification(String.format("%s joined the game as an observer", username));
            broadcastMessage(gameID, broadcastMessage, authToken);
            ServerMessage soloMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            var myGame = gameDAO.getGame(gameID);
            if (myGame.game() == null) {
                gameDAO.createNewPlayableGame(myGame);
                myGame = gameDAO.getGame(gameID);
            }
            soloMessage.setGame(myGame.game());
            sendMessage(gameID, soloMessage, authToken);
        } catch (IOException e) {
            throw new DataAccessException(500, e.getMessage());
        }
    }
    public void makeMove(int gameID, chess.ChessMove move, String authToken, Session session) throws DataAccessException {
        try {
            ChessGame game = gameDAO.getGame(gameID).game();
            model.Game modelGame = gameDAO.getGame(gameID);
            if (game.getTeamTurn() == null){
                throw new DataAccessException(400, "Game is already over");
            }
            String username = getUsername(authToken);
            if (game.getTeamTurn() == ChessGame.TeamColor.WHITE){
                if (!modelGame.whiteUsername().equals(username)){
                    throw new DataAccessException(400, "It's not your turn");
                }
            } else if (game.getTeamTurn() == ChessGame.TeamColor.BLACK) {
                if (!modelGame.blackUsername().equals(username)) {
                    throw new DataAccessException(400, "It's not your turn");
                }
            }
            try {
                game.makeMove(move);
            } catch (InvalidMoveException e){
                throw new DataAccessException(400, e.getMessage());
            }
            model.Game updatedGame = new model.Game(modelGame.gameName(), modelGame.gameID(), modelGame.whiteUsername(), modelGame.blackUsername(), game);
            gameDAO.updatePlayableGame(updatedGame);
            ServerMessage moveMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            moveMessage.setNotification(String.format("%s moved %s from %s to %s", username,
                    game.getBoard().getPiece(move.getStartPosition()), move.getStartPosition(), move.getEndPosition()));
            broadcastMessage(gameID, moveMessage, authToken);
            ServerMessage gameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            gameMessage.setGame(game);
            sendAll(gameID, gameMessage, authToken);
            if (game.isInCheck(ChessGame.TeamColor.WHITE) || game.isInCheck(ChessGame.TeamColor.BLACK)) {
                if (game.isInCheck(ChessGame.TeamColor.WHITE)){
                    if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                        ServerMessage checkmateWhiteMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                        checkmateWhiteMessage.setNotification(String.format("%s is in checkmate %s wins",
                                gameDAO.getGame(gameID).whiteUsername(),gameDAO.getGame(gameID).blackUsername()));
                        sendAll(gameID, checkmateWhiteMessage, authToken);
                    }
                    else {
                        ServerMessage checkWhiteMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                        checkWhiteMessage.setNotification(String.format("%s is in check",
                                gameDAO.getGame(gameID).whiteUsername()));
                        sendAll(gameID, checkWhiteMessage, authToken);
                    }
                }
                if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                    if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                        ServerMessage checkmateBlackMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                        checkmateBlackMessage.setNotification(String.format("%s is in checkmate %s wins",
                                gameDAO.getGame(gameID).blackUsername(),gameDAO.getGame(gameID).whiteUsername()));
                        sendAll(gameID, checkmateBlackMessage, authToken);
                    }
                    else {
                        ServerMessage checkBlackMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                        checkBlackMessage.setNotification(String.format("%s is in check",
                                gameDAO.getGame(gameID).blackUsername()));
                        sendAll(gameID, checkBlackMessage, authToken);
                    }
                }
            }
            else {
                if (game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                }
                if (game.isInStalemate(ChessGame.TeamColor.WHITE)) {
                }
            }
        } catch (IOException e) {
            throw new DataAccessException(500, e.getMessage());
        }
    }
    public void leave(int gameID, String authToken, Session session) throws DataAccessException {
        try {
            String username = getUsername(authToken);
            model.Game game = gameDAO.getGame(gameID);
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            serverMessage.setNotification(String.format("%s left the game", username));
            broadcastMessage(gameID, serverMessage, authToken);
            if (game.whiteUsername() != null) {
                if (game.whiteUsername().equals(username)) {
                    gameDAO.updateGame(gameID, ChessGame.TeamColor.WHITE, null);
                }
            }
            if (game.blackUsername() != null){
                if (game.blackUsername().equals(username)) {
                    gameDAO.updateGame(gameID, ChessGame.TeamColor.BLACK, null);
                }
            }
            sessions.removeSession(session);
        } catch (IOException e) {
            throw new DataAccessException(500, e.getMessage());
        }
    }
    public void resign(int gameID, String authToken, Session session) throws DataAccessException{
        model.Game myGame = gameDAO.getGame(gameID);
        if (!(getUsername(authToken).equals(myGame.blackUsername()) || getUsername(authToken).equals(myGame.whiteUsername()))){
            throw new DataAccessException(400, "Only a player can resign");
        }
        if (myGame.game().getTeamTurn() == null){
            throw new DataAccessException(400, "Game is already over");
        }
        else {
            myGame.game().setGameOver();
            gameDAO.updatePlayableGame(myGame);
        }
        ServerMessage resignMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        resignMessage.setNotification(String.format("%s resigned", getUsername(authToken)));
        try{
            sendAll(gameID, resignMessage, authToken);
        } catch (IOException e){
            throw new DataAccessException(500, e.getMessage());
        }
    }
    public void closeSession(Session session){
        sessions.removeSession(session);
    }
    private void sendMessage(int gameID, ServerMessage message, String authToken) throws IOException {
        var sessionsForGame = this.sessions.getSessionsForGame(gameID);
        var session = sessionsForGame.get(authToken);
        session.getRemote().sendString(new Gson().toJson(message));
    }
    private void broadcastMessage(int gameID, ServerMessage message, String exceptThisAuthToken) throws IOException{
        var removeList = new ArrayList<Session>();
        for (var session : sessions.getSessionsForGame(gameID).values()) {
            if (session.isOpen()) {
                if (!sessions.getSessionsForGame(gameID).get(exceptThisAuthToken).equals(session)) {
                    session.getRemote().sendString(new Gson().toJson(message));
                }
            } else {
                removeList.add(session);
            }
        }

        // Clean up any connections that were left open.
        for (var mySession : removeList) {
            sessions.removeSession(mySession);
        }
    }
    public void updateUsernames() throws DataAccessException{
        for (var gameID:sessions.getSessionsMap().keySet()) {
            for (var auth : sessions.getSessionsForGame(gameID).keySet()) {
                model.Auth removeAuth = authDAO.getAuth(auth);
                if (gameDAO.getGame(gameID).whiteUsername().equals(removeAuth.username())) {
                    if (!sessions.getSessionsForGame(gameID).get(removeAuth.authToken()).isOpen()) {
                        gameDAO.updateGame(gameID, ChessGame.TeamColor.WHITE, null);
                    }
                } else if (gameDAO.getGame(gameID).blackUsername().equals(removeAuth.username())) {
                    if (!sessions.getSessionsForGame(gameID).get(removeAuth.authToken()).isOpen()) {
                        gameDAO.updateGame(gameID, ChessGame.TeamColor.BLACK, null);
                    }                }
            }
        }
    }
    private void sendAll(int gameID, ServerMessage message, String authToken) throws IOException{
        broadcastMessage(gameID, message, authToken);
        sendMessage(gameID, message, authToken);
    }
}
