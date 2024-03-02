package dataAccess.gameDAOs;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.Game;

import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO{
    public SQLGameDAO(){
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.configTable(createGameTable);
        }
        catch(DataAccessException ex) {
            System.out.println(ex.getStatusCode() + String.format(", \"message\" Error: %s", ex.getMessage()));
        }
    }
    @Override
    public Collection<Game> listGames(){
        return new ArrayList<>();
    }
    @Override
    public int createGame(Game game) {
        return 0;
    }
    @Override
    public void clear(){}
    @Override
    public void updateGame(int gameID, ChessGame.TeamColor playerColor, String username) {}
    @Override
    public Game getGame(int gameID){
        return new Game("null",0,"null","null",new ChessGame());
    }

    private final String[] createGameTable = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameName` varchar(256) NOT NULL,
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `chessGame` varchar(256),
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
