package model;
import chess.*;

public record Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
}
