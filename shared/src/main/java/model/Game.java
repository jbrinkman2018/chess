package model;
import chess.*;

public record Game(String gameName, int gameID, String whiteUsername, String blackUsername, ChessGame game) {
}
