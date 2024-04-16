package server.webSocket;

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
            if (playerColor.equals(ChessGame.TeamColor.WHITE)){
                if (gameDAO.getGame(gameID).whiteUsername() != null){
                    throw new DataAccessException(400, "White username already taken");
                }
            } else if (playerColor.equals(ChessGame.TeamColor.BLACK)) {
                if (gameDAO.getGame(gameID).blackUsername() != null){
                    throw new DataAccessException(400, "Black username already taken");
                }
            }
            gameDAO.updateGame(gameID, playerColor, username);
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
    public void makeMove(int gameID, chess.ChessMove move, String authToken, Session session) {}
    public void leave(int gameID, String authToken, Session session) throws DataAccessException {
        try {
            String username = getUsername(authToken);
            model.Game game = gameDAO.getGame(gameID);
            if (game.whiteUsername().equals(username)) {
                gameDAO.updateGame(gameID, ChessGame.TeamColor.WHITE, null);
            } else if (game.blackUsername().equals(username)) {
                gameDAO.updateGame(gameID, ChessGame.TeamColor.BLACK, null);
            }
            sessions.removeSession(session);
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            serverMessage.setNotification(String.format("%s left the game", username));
            broadcastMessage(gameID, serverMessage, authToken);
        } catch (IOException e) {
            throw new DataAccessException(500, e.getMessage());
        }
    }
    public void resign(int gameID, String authToken, Session session){}
    public void closeSession(Session session){}
    private void sendMessage(int gameID, ServerMessage message, String authToken) throws IOException {
        var sessionsForGame = this.sessions.getSessionsForGame(gameID);
        var session = sessionsForGame.get(authToken);
        session.getRemote().sendString(new Gson().toJson(message));
    }
    private void broadcastMessage(int gameID, ServerMessage message, String exceptThisAuthToken) throws IOException {
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
        for (var session : removeList) {
            sessions.removeSession(session);
        }


//        var sessionsForGame = this.sessions.getSessionsForGame(gameID);
//        for (var authToken:sessionsForGame.keySet()){
//            if (!authToken.equals(exceptThisAuthToken)){
//                sendMessage(gameID, message, authToken);
//            }
//        }
    }
}
