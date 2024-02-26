package dataAccess.gameDAOs;

import model.Game;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private final HashMap<Integer, Game> games = new HashMap<>();
    private int gameIDTally;
    @Override
    public Collection<Game> listGames(){
        return games.values();
    }
    @Override
    public int createGame(Game game) {
        if (games.size() == 0) {
            gameIDTally = 1;
        }
        int gameID = gameIDTally;
        gameIDTally++;
        games.put(gameID, game);
        return gameID;
    }
    @Override
    public void clear(){
        games.clear();
    }
    @Override
    public void joinGame(int gameID, String playerColor) {
        Game myGame = games.get(gameID);
    }

}
