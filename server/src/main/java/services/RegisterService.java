package services;

import model.*;
import dataAccess.*;

import javax.xml.crypto.Data;

public class RegisterService implements Service{
    public RegisterService() {}
    public String register(User user) throws DataAccessException {
        UserDAO userDAO = new MemoryUserDAO();
        if (userDAO.getUser(user.getUsername()) == null) {
            throw new DataAccessException(403, "Error: already taken");
        }
        userDAO.createUser(user);
        AuthDAO authDAO = new MemoryAuthDAO();
        return authDAO.createAuth(user.getUsername());
    }
}
