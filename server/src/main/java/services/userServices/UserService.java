package services.userServices;
import dataAccess.authDAOs.AuthDAO;
import dataAccess.userDAOs.UserDAO;
import services.Service;

public abstract class UserService implements Service {
    protected UserDAO userDAO;
    protected AuthDAO authDAO;
    protected UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
}
