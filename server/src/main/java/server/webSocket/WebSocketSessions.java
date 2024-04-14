package server.webSocket;

import org.eclipse.jetty.websocket.api.Session;
import services.gameServices.GameService;

import java.util.HashMap;
import java.util.Map;

public class WebSocketSessions {
    private Map<Integer, Map<String, Session>> sessionMap;
    WebSocketSessions() {
        this.sessionMap = new HashMap<>();
    }
    public void addSessionToGame(int gameID, String authToken, Session session) {
        Map<String, Session> curSession = new HashMap<>();
        curSession.put(authToken, session);
        sessionMap.put(gameID, curSession);
    }
    public void removeSessionFromGame(int gameID, String authToken) {
        sessionMap.get(gameID).remove(authToken);
    }
    public void removeSession(Session session){

    }
    public Map<String,Session> getSessionsForGame(int gameID){
        return sessionMap.get(gameID);
    }
}
