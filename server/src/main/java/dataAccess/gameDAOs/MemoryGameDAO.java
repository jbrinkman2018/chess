package dataAccess.gameDAOs;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private final HashMap<Integer, Game> games = new HashMap<>();
    private int gameIDTally = 1;
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
        Game createdGame = new Game(game.gameName(), gameID, null, null, null);
        games.put(gameID, createdGame);
        return gameID;
    }
    @Override
    public void clear(){
        games.clear();
    }
    @Override
    public void updateGame(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException {
        Game myGame = games.get(gameID);
        Game updatedGame;
        if (playerColor == ChessGame.TeamColor.WHITE) {
            if (myGame.whiteUsername() != null){
                throw new DataAccessException(403, "already taken");
            }
            updatedGame = new Game(myGame.gameName(), myGame.gameID(), username, myGame.blackUsername(),myGame.game());
        }
        else if (playerColor == ChessGame.TeamColor.BLACK){
            if (myGame.blackUsername() != null){
                throw new DataAccessException(403, "already taken");
            }
            updatedGame = new Game(myGame.gameName(), myGame.gameID(), myGame.whiteUsername(), username, myGame.game());
        }
        else {
            return;
        }
        games.remove(gameID);

        games.put(gameID, updatedGame);
    }
    @Override
    public Game getGame(int gameID){
        return games.get(gameID);
    }
}
