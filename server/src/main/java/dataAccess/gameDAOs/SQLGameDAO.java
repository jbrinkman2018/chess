package dataAccess.gameDAOs;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.Auth;
import model.Game;

import java.util.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLGameDAO implements GameDAO{
    private int gameIDTally = 1;
    public SQLGameDAO(){
        try {
            DatabaseManager.configTable(createGameTable);
            try (var conn = DatabaseManager.getConnection()) {
                var statement = "SELECT MAX(gameID) from game";
                try (var ps = conn.prepareStatement(statement)) {
                    try (var rs = ps.executeQuery()) {
                        if (rs.next()) {
                            gameIDTally = Integer.parseInt(rs.getString("MAX(gameID)")) + 1;
                        }
                    }
                }
            } catch (Exception e) {
                throw new DataAccessException(500, String.format("Unable to read data: %s", e.getMessage()));
            }
        }
        catch(DataAccessException ex) {
            System.out.println(ex.getStatusCode() + String.format(", \"message\" Error: %s", ex.getMessage()));
        }
    }
    @Override
    public Collection<Game> listGames(){
        var result = new ArrayList<Game>();
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var statement = "SELECT gameName, gameID, whiteUsername, blackUsername, chessGame FROM game";
                try (var ps = conn.prepareStatement(statement)) {
                    try (var rs = ps.executeQuery()) {
                        while (rs.next()) {
                            result.add(readGame(rs));
                        }
                    }
                }
            } catch (Exception e) {
                throw new DataAccessException(500, String.format("Unable to read data: %s", e.getMessage()));
            }
        }
        catch (DataAccessException ex) {
            System.out.println(String.format("Error:", ex.getMessage()));
        }
        return result;
    }
    @Override
    public int createGame(Game game) {
        int gameID = gameIDTally;
        gameIDTally++;
        Game myGame = new Game(game.gameName(), gameID, "null", "null", null);
        String SQLCreateGame = "INSERT INTO game (gameName, gameID, whiteUsername, blackUsername, chessGame) VALUES (?, ?, ?, ?, ?)";
        try {
            DatabaseManager.createGameUpdate(SQLCreateGame, myGame.gameName(), gameID, null, null, null);
        }
        catch (DataAccessException ex) {
            System.out.println(String.format("Error:", ex.getMessage()));
        }
        return gameID;
    }
    @Override
    public void clear(){
        String SQLClearGames = "DELETE FROM game";
        try {
            DatabaseManager.executeUpdate(SQLClearGames);
        }
        catch (DataAccessException ex) {
            System.out.println(String.format("Error:", ex.getMessage()));
        }
        gameIDTally = 1;
    }
    @Override
    public Game getGame(int gameID){
        try {
            try (var conn = DatabaseManager.getConnection()) {
                var statement = "SELECT * FROM game WHERE gameID = '" + gameID + "'";
                try (var ps = conn.prepareStatement(statement)) {
                    try (var rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return readGame(rs);
                        }
                    }
                }
            } catch (Exception e) {
                throw new DataAccessException(500, String.format("Unable to read data: %s", e.getMessage()));
            }
        }catch (DataAccessException ex) {
            System.out.println(String.format("Error:", ex.getMessage()));
        }
        return null;
    }
    @Override
    public void updateGame(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException{
        if (playerColor == null ) {
        }
        else {
            String SQLUpdateGame = "UPDATE game SET " + playerColor.toString().toLowerCase() + "username = '"
                    + username + "' WHERE gameID = " + gameID;
            if (playerColor != ChessGame.TeamColor.BLACK && playerColor != ChessGame.TeamColor.WHITE) {
                return;
            }
            if ((getGame(gameID).whiteUsername() != null) && (playerColor == ChessGame.TeamColor.WHITE)) {
                throw new DataAccessException(403, "already taken");
            }
            if ((getGame(gameID).blackUsername() != null) && (playerColor == ChessGame.TeamColor.BLACK)) {
                throw new DataAccessException(403, "already taken");
            }
            try {
                DatabaseManager.executeUpdate(SQLUpdateGame);
            } catch (DataAccessException ex) {
                System.out.println(String.format("Error:", ex.getMessage()));
            }
        }
    }
    private final String[] createGameTable = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameName` varchar(256) NOT NULL,
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `chessGame` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
    private Game readGame(ResultSet rs) throws SQLException {
        var gameName = rs.getString("gameName");
        var gameID = rs.getInt("gameID");
//        String whiteUsername = null;
//        if (!rs.getString("whiteUsername").equals("null")) {
        var whiteUsername = rs.getString("whiteUsername");
//        }
//        String blackUsername = null;
//        if (!rs.getString("blackUsername").equals("null")) {
      var blackUsername = rs.getString("blackUsername");
//        }
//        String jsonChessGame = null;
//        if (!rs.fgetString("chessGame").equals("null")) {
        var jsonChessGame = rs.getString("chessGame");
//        }
        var chessGame = new Gson().fromJson(jsonChessGame, ChessGame.class);
        return new Game(gameName, gameID, whiteUsername, blackUsername, chessGame);
    }
    @Override
    public void createNewPlayableGame(Game game) throws DataAccessException{
            try {
                String SQLUpdateGame = "UPDATE game SET ChessGame = '"
                        + new Gson().toJson(new ChessGame()) + "' WHERE gameID = " + game.gameID();
                DatabaseManager.executeUpdate(SQLUpdateGame);
            } catch (DataAccessException ex) {
                System.out.println(String.format("Error:", ex.getMessage()));
            }
    }
}
