package dataAccess.gameDAOs;

import chess.ChessGame;
import model.Game;

import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO{
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
}
