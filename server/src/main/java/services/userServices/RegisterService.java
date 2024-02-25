package services.userServices;

import dataAccess.authDAOs.AuthDAO;
import dataAccess.userDAOs.UserDAO;
import model.*;
import dataAccess.*;

public class RegisterService extends UserService{
    public RegisterService(UserDAO userDAO, AuthDAO authDAO){
        super(userDAO, authDAO);
    }
    public Auth register(User user) throws DataAccessException {
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() ==null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        if (userDAO.getUser(user.getUsername()) != null) {
            throw new DataAccessException(403, "Error: already taken");
        }
        userDAO.createUser(user);
        String myAuthToken = authDAO.createAuth(user.getUsername());
        return authDAO.getAuth(myAuthToken);
    }

}
