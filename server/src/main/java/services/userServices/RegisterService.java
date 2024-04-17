package services.userServices;

import DataAccessException.DataAccessException;
import dataAccess.authDAOs.AuthDAO;
import dataAccess.userDAOs.UserDAO;
import model.*;

public class RegisterService extends UserService{
    public RegisterService(UserDAO userDAO, AuthDAO authDAO){
        super(userDAO, authDAO);
    }
    public Auth register(User user) throws DataAccessException {
        if (user.username() == null || user.password() == null || user.email() ==null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        if (userDAO.getUser(user.username()) != null) {
            throw new DataAccessException(403, "Error: already taken");
        }
        userDAO.createUser(user);
        return authDAO.createAuth(user.username());
    }

}
