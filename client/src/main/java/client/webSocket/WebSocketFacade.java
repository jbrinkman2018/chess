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

public class WebSocketFacade extends Endpoint implements MessageHandler.Whole<String>{
    private Session session;
    private GameHandler gameHandler;
    public WebSocketFacade(String url, GameHandler gameHandler) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.gameHandler = gameHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
//            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
//                @Override
//                public void onMessage(String message) {
//                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
////                    gameHandler.updateGame();
//                }
//            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
//            System.out.println(ex.getClass());
            throw new DataAccessException(500, ex.getMessage());
        }
    }
    @Override
    public void onOpen(Session session, EndpointConfig epConfig){}
    public void onClose(){}
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
    public void joinObserver(int gameID, String authToken){
        UserGameCommand userCmd = new UserGameCommand(authToken, gameID);
        userCmd.setCommandType(UserGameCommand.CommandType.JOIN_OBSERVER);
    }
    public void makeMove(int gameID, String authToken) {
        UserGameCommand userCmd = new UserGameCommand(authToken, gameID);
        userCmd.setCommandType(UserGameCommand.CommandType.MAKE_MOVE);
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
    public void resignGame(int gameID, String authToken){
        UserGameCommand userCmd = new UserGameCommand(authToken, gameID);
        userCmd.setCommandType(UserGameCommand.CommandType.RESIGN);
    }

    private void sendMessage(){}

    // process incoming message
    @Override
    public void onMessage(String message) {
        ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
        switch (msg.getServerMessageType()) {
            case LOAD_GAME -> gameHandler.updateGame(msg.getGame());
            case NOTIFICATION -> gameHandler.printMessage(msg.getNotification());
            case ERROR -> msg.getErrorMessage();
        }
    }
}
