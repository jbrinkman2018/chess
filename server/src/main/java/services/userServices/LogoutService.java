package services.userServices;

import dataAccess.authDAOs.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.userDAOs.UserDAO;

public class LogoutService extends UserService{
    public LogoutService(UserDAO userDAO, AuthDAO authDAO){
        super(userDAO, authDAO);
    }
    public void logout(String auth) {
        authDAO.deleteAuth(auth);
    }
    public void verifyAuth(String auth) throws DataAccessException{
        if (authDAO.getAuth(auth) == null) {
            throw new DataAccessException(401, "unauthorized");
        }
    }
}
