package webSocketMessages.userCommands;

import java.util.Objects;
import chess.*;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public UserGameCommand(String authToken, int gameID) {
        this.authToken = authToken;
        this.gameID = gameID;
    }
    public UserGameCommand(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }


    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;

    protected String authToken;

    protected int gameID;
    private ChessGame.TeamColor playerColor;

    public int getGameID() {
        return gameID;
    }
    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }
    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }
    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
    public void getPlayerColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}
