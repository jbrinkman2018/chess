package server.webSocket;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.authDAOs.AuthDAO;
import dataAccess.authDAOs.SQLAuthDAO;
import dataAccess.gameDAOs.GameDAO;
import dataAccess.gameDAOs.SQLGameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.*;

@WebSocket
public class WebSocketHandler {
    private AuthDAO authDAO = new SQLAuthDAO();
    private GameDAO gameDAO = new SQLGameDAO();
    private WebSocketServices gameService = new WebSocketServices(gameDAO, authDAO);

    @OnWebSocketConnect
    public void onConnect(Session session) {
    }
    @OnWebSocketClose
    public void onClose(Session session,int closeInt, String str) { gameService.closeSession(session);
    }
    @OnWebSocketError
    public void onError(Throwable throwable) {
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String str) throws DataAccessException {
        UserGameCommand cmd = new Gson().fromJson(str, UserGameCommand.class);
        switch (cmd.getCommandType()) {
            case JOIN_PLAYER -> gameService.joinPlayer(cmd.getGameID(), cmd.getAuthString(), cmd.getPlayerColor(), session);
            case JOIN_OBSERVER -> gameService.joinObserver(cmd.getAuthString(), session);
            case MAKE_MOVE -> gameService.makeMove(cmd.getAuthString(), session);
            case LEAVE -> gameService.leave(cmd.getGameID(),cmd.getAuthString(), session);
            case RESIGN -> gameService.resign(cmd.getAuthString(), session);
        }
    }
}
