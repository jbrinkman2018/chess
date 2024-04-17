package server.webSocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.Map;

public class WebSocketSessions {
    private Map<Integer, Map<String, Session>> sessionMap;
    WebSocketSessions() {
        this.sessionMap = new HashMap<>();
    }
    public void addSessionToGame(int gameID, String authToken, Session session) {
        Map<String, Session> curSession = null;
        if (sessionMap.get(gameID) == null) {
            curSession = new HashMap<>();
        } else {
            curSession = sessionMap.get(gameID);
        }
        curSession.put(authToken, session);
        sessionMap.put(gameID, curSession);
    }
    public void removeSessionFromGame(int gameID, String authToken) {
        sessionMap.get(gameID).remove(authToken);
    }
    public Map<Integer, Map<String, Session>> getSessionsMap() {
        return sessionMap;
    }
    public void removeSession(Session session){
        for (int gameID:sessionMap.keySet()){
            for(String authToken:sessionMap.get(gameID).keySet()){
                if(sessionMap.get(gameID).get(authToken).equals(session)){
                    removeSessionFromGame(gameID,authToken);
                }
            }
        }
    }
    public Map<String,Session> getSessionsForGame(int gameID){
        return sessionMap.get(gameID);
    }
}
