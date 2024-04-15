package server.webSocket;

import dataAccess.DataAccessException;
import dataAccess.authDAOs.AuthDAO;
import dataAccess.gameDAOs.GameDAO;
import org.eclipse.jetty.websocket.api.Session;

import server.Server;
import services.gameServices.GameService;
import webSocketMessages.serverMessages.*;
import chess.ChessGame;
public class WebSocketServices extends GameService {
    private final WebSocketSessions sessions = new WebSocketSessions();

    public WebSocketServices(GameDAO gameDAO, AuthDAO authDAO) {
        super(gameDAO, authDAO);
    }

    public void joinPlayer(int gameID, String authToken, ChessGame.TeamColor playerColor, Session session) throws DataAccessException {
        String username = getUsername(authToken);
        gameDAO.updateGame(gameID, playerColor, username);
        sessions.addSessionToGame(gameID,authToken, session);
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        serverMessage.setNotification(String.format("%s joined the game as %s", username, playerColor));
        broadcastMessage(gameID, serverMessage, authToken);
    }
    public void joinObserver(String authToken, Session session){}
    public void makeMove(String authToken, Session session) {}
    public void leave(String authToken, Session session) {}
    public void resign(String authToken, Session session){}
    public void closeSession(Session session){}
    private void sendMessage(int gameID, ServerMessage message, String authToken) {
        var sessionsForGame = this.sessions.getSessionsForGame(gameID);
        var session = sessionsForGame.get(authToken);
        session.getRemote().
    }
    private void broadcastMessage(int gameID, ServerMessage message, String exceptThisAuthToken) {
        var sessionsForGame = this.sessions.getSessionsForGame(gameID);
        for (var authToken:sessionsForGame.keySet()){
            if (!authToken.equals(exceptThisAuthToken)){
                sendMessage(gameID, message, authToken);
            }
        }
    }
}
