package dataAccess.gameDAOs;
import model.*;
import java.util.Collection;
public interface GameDAO {
    Collection<Game> listGames();
    int createGame(Game game);
    void clear();
    void joinGame(int gameID, String playerColor);
}
