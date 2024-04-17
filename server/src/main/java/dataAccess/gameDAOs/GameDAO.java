package dataAccess.gameDAOs;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.*;

import javax.xml.crypto.Data;
import java.util.Collection;
public interface GameDAO {
    Collection<Game> listGames();
    int createGame(Game game);
    void clear();
    void updateGame(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException;
    Game getGame(int gameID);
    void createNewPlayableGame(Game game) throws DataAccessException;
    public void updatePlayableGame(Game game) throws DataAccessException;
//    void removePlayerFromGame(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException;
}
