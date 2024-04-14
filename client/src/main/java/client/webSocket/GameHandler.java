package client.webSocket;
import model.Game;

public interface GameHandler {
    public void updateGame(Game game);
    public void printMessage(String message);
}
