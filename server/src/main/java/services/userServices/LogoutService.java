package services.userServices;

import dataAccess.authDAOs.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.userDAOs.UserDAO;
import model.*;

public class LogoutService extends UserService{
    public LogoutService(UserDAO userDAO, AuthDAO authDAO){
        super(userDAO, authDAO);
    }
    public void logout(String auth) throws DataAccessException {
        authDAO.deleteAuth(auth);
    }
}
