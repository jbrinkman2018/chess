package services;

import dataAccess.authDAOs.AuthDAO;
import dataAccess.userDAOs.UserDAO;
import dataAccess.gameDAOs.GameDAO;

public class ClearService implements Service{
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    public ClearService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public void clear() {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

}
