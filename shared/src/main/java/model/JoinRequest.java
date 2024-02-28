package model;
import chess.*;

public record JoinRequest(ChessGame.TeamColor playerColor, int gameID) {
}
