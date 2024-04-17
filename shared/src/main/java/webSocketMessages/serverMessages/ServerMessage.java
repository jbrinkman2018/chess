package webSocketMessages.serverMessages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    String message;
    String errorMessage;
    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }
    private chess.ChessGame game;

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }
    public void setGame(chess.ChessGame game) {
        this.game = game;
    }
    public chess.ChessGame getGame() {
        return game;
    }
    public void setNotification(String message) {
        this.message = message;
    }
    public String getNotification() {
        return this.message;
    }
    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage(){
        return this.errorMessage;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServerMessage))
            return false;
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
