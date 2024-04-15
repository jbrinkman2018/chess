package webSocketMessages.userCommands;

import chess.ChessGame;
import model.User;

public class JoinPlayerGameCommand extends UserGameCommand {
    private ChessGame.TeamColor playerColor;

    public JoinPlayerGameCommand(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }
    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
