package model;

public record JoinGameInfo(String playerColor, int gameID) {
    public int getGameID() {
        return gameID;
    }
    public String getPlayerColor() {
        return playerColor;
    }
}
