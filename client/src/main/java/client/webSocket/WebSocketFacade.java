package client.webSocket;

import com.google.gson.Gson;
import dataAccess.DataAccessException;

import javax.websocket.*;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;
import javax.websocket.*;

import chess.*;

public class WebSocketFacade extends Endpoint {
    private Session session;
    private GameHandler gameHandler;
    public WebSocketFacade(String url, GameHandler gameHandler) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.gameHandler = gameHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
                    switch (msg.getServerMessageType()) {
                        case LOAD_GAME -> gameHandler.updateGame(msg.getGame());
                        case NOTIFICATION -> gameHandler.printMessage(msg.getNotification());
                        case ERROR -> handleServerError(msg);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new DataAccessException(500, ex.getMessage());
        }
    }
    @Override
    public void onOpen(Session session, EndpointConfig epConfig){
    }
    public void onClose(){
    }
    public void onError(){}

    // outgoing messages
    public void joinPlayer(int gameID, String authToken, ChessGame.TeamColor playerColor) throws DataAccessException{
        try{
            UserGameCommand userCmd = new UserGameCommand(authToken, gameID, playerColor);
            userCmd.setCommandType(UserGameCommand.CommandType.JOIN_PLAYER);
            session.getBasicRemote().sendText(new Gson().toJson(userCmd));
        } catch (IOException e){
            throw new DataAccessException(500, e.getMessage());
        }
    }
    public void joinObserver(int gameID, String authToken) throws DataAccessException{
        try{
        UserGameCommand userCmd = new UserGameCommand(authToken, gameID);
        userCmd.setCommandType(UserGameCommand.CommandType.JOIN_OBSERVER);
        session.getBasicRemote().sendText(new Gson().toJson(userCmd));
        } catch (IOException e){
            throw new DataAccessException(500, e.getMessage());
        }
    }
    public void makeMove(int gameID, String authToken, ChessMove chessMove) throws DataAccessException{
        try{
            UserGameCommand userCmd = new UserGameCommand(authToken, gameID, chessMove);
            userCmd.setCommandType(UserGameCommand.CommandType.MAKE_MOVE);
            session.getBasicRemote().sendText(new Gson().toJson(userCmd));
        } catch (IOException e){
            throw new DataAccessException(500, e.getMessage());
        }
    }
    public void leaveGame(int gameID, String authToken) throws DataAccessException{
        try{
            UserGameCommand userCmd = new UserGameCommand(authToken, gameID);
            userCmd.setCommandType(UserGameCommand.CommandType.LEAVE);
            session.getBasicRemote().sendText(new Gson().toJson(userCmd));
            this.session.close();
        } catch (IOException e){
            throw new DataAccessException(500, e.getMessage());
        }
    }
    public void resignGame(int gameID, String authToken) throws DataAccessException{
        try{
            UserGameCommand userCmd = new UserGameCommand(authToken, gameID);
            userCmd.setCommandType(UserGameCommand.CommandType.RESIGN);
            session.getBasicRemote().sendText(new Gson().toJson(userCmd));
        } catch (IOException e){
            throw new DataAccessException(500, e.getMessage());
        }
    }

    private void sendMessage(){}
    private void handleServerError(ServerMessage error){
        System.out.println(400 + ", " + error.getErrorMessage());
    }
}
