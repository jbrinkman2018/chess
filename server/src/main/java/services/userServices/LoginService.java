package services.userServices;

import dataAccess.authDAOs.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.userDAOs.UserDAO;
import model.*;

public class LoginService extends UserService{
    public LoginService(UserDAO userDAO, AuthDAO authDAO) {
        super(userDAO, authDAO);
    }
    public Auth login(User user) throws DataAccessException{
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        if (userDAO.getUser(user.getUsername()) == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        if (!user.getPassword().equals(userDAO.getUser(user.username()).getPassword())) {
            throw new DataAccessException(401, "Error: unauthorized");
        }
        String myAuthToken = authDAO.createAuth(user.username());
        return authDAO.getAuth(myAuthToken);
    }
}
