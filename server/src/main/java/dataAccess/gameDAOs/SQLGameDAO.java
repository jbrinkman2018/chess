package dataAccess.gameDAOs;

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
    public void clear(){

    }
}
