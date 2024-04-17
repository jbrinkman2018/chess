package services.gameServices;

import DataAccessException.DataAccessException;
import dataAccess.authDAOs.AuthDAO;
import dataAccess.gameDAOs.GameDAO;
import services.Service;

public abstract class GameService implements Service {
    protected GameDAO gameDAO;
    protected AuthDAO authDAO;
    protected GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public void verifyAuth(String auth) throws DataAccessException{
        if (authDAO.getAuth(auth) == null) {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }
    public String getUsername(String authToken){
        return authDAO.getAuth(authToken).username();
    }
}
