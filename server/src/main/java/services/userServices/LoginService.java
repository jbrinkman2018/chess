package services.userServices;

import dataAccess.authDAOs.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.userDAOs.UserDAO;
import model.*;

import javax.xml.crypto.Data;

public class LoginService extends UserService{
    public LoginService(UserDAO userDAO, AuthDAO authDAO) {
        super(userDAO, authDAO);
    }
    public Auth login(User user) throws DataAccessException {
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        if (userDAO.getUser(user.getUsername()) == null) {
            throw new DataAccessException(401, "Error: unauthorized");
        }
        if (!user.getPassword().equals(userDAO.getUser(user.username()).getPassword())) {
            throw new DataAccessException(401, "Error: unauthorized");
        }
        return authDAO.createAuth(user.username());
    }
}
