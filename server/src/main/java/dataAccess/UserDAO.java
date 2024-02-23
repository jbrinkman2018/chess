package dataAccess;

import model.*;

public interface UserDAO {
    public User getUser(String username);
    public void createUser(User user);
}
