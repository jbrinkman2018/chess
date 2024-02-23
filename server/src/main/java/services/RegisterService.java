package services;

import model.*;
import dataAccess.*;

public class RegisterService implements Service{
    public RegisterService() {}
    public String register(User user) {
        UserDAO userDAO = new MemoryUserDAO();
        if (userDAO.getUser(user.getUsername()) == null) {
            // throw exception username already in use
        }
        userDAO.createUser(user);
        AuthDAO authDAO = new MemoryAuthDAO();
        return authDAO.createAuth(user.getUsername());
    }
}
