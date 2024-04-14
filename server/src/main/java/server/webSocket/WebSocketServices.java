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

    public WebSocketServices(GameDAO gameDAO, AuthDAO authDAO) {
        super(gameDAO, authDAO);
    }
    private WebSocketSessions sessions = new WebSocketSessions();

    public void joinPlayer(int gameID, String authToken, ChessGame.TeamColor playerColor, Session session) throws DataAccessException {
        String username = getUsername(authToken);
//        gameDAO.updateGame(gameID, playerColor, username);
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
    private void sendMessage(int gameID, String message, String authToken) {

    }
    private void broadcastMessage(int gameID, ServerMessage message, String exceptThisAuthToken) {

    }
}
