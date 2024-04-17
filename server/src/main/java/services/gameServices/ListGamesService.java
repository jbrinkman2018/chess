package services.gameServices;

import DataAccessException.DataAccessException;
import dataAccess.authDAOs.AuthDAO;
import dataAccess.gameDAOs.GameDAO;
import model.*;

import java.util.Collection;

public class ListGamesService extends GameService {
    public ListGamesService(GameDAO gameDAO, AuthDAO authDAO) {
        super(gameDAO, authDAO);
    }
    public Collection<Game> listGames() throws DataAccessException {
        return gameDAO.listGames();
    }
}
