package server.webSocket;

import com.google.gson.Gson;
import DataAccessException.DataAccessException;
import dataAccess.authDAOs.AuthDAO;
import dataAccess.authDAOs.SQLAuthDAO;
import dataAccess.gameDAOs.GameDAO;
import dataAccess.gameDAOs.SQLGameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.*;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private AuthDAO authDAO = new SQLAuthDAO();
    private GameDAO gameDAO = new SQLGameDAO();
    private Session session;
    private WebSocketServices gameService;
    public WebSocketHandler(){
        gameService = new WebSocketServices(gameDAO, authDAO);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
//        try {
//            gameService.updateUsernames();
//        }catch (DataAccessException e){
//            onError(e);
//        }
    }
    @OnWebSocketClose
    public void onClose(Session session, int closeID, String closeString) {
        gameService.closeSession(session);
//        try {
//            gameService.updateUsernames();
//        }catch (DataAccessException e){
//            onError(e);
//        }
    }
    @OnWebSocketError
    public void onError(Throwable throwable){
        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        msg.setErrorMessage(throwable.getMessage());
        try{
            session.getRemote().sendString(new Gson().toJson(msg));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String str){
        try {
            this.session = session;
            UserGameCommand cmd = new Gson().fromJson(str, UserGameCommand.class);
            switch (cmd.getCommandType()) {
                case JOIN_PLAYER -> gameService.joinPlayer(cmd.getGameID(), cmd.getAuthString(), cmd.getPlayerColor(), session);
                case JOIN_OBSERVER -> gameService.joinObserver(cmd.getGameID(), cmd.getAuthString(), session);
                case MAKE_MOVE -> gameService.makeMove(cmd.getGameID(), cmd.getMove(), cmd.getAuthString(), session);
                case LEAVE -> gameService.leave(cmd.getGameID(), cmd.getAuthString(), session);
                case RESIGN -> gameService.resign(cmd.getGameID(), cmd.getAuthString(), session);
            }
        } catch (DataAccessException e){
            onError(e);
        }
    }
}
