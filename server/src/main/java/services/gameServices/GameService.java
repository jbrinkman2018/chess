package services.gameServices;

import dataAccess.DataAccessException;
import dataAccess.authDAOs.AuthDAO;
import dataAccess.gameDAOs.GameDAO;
import services.Service;
import model.*;

public abstract class GameService implements Service {
    protected GameDAO gameDAO;
    protected AuthDAO authDAO;
    protected GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public void verifyAuth(Auth auth) throws DataAccessException{
        if (authDAO.getAuth(auth.getAuthToken()) == null) {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }
}
