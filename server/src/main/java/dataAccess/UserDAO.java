package dataAccess;

import model.*;

public interface UserDAO {
    public User getUser(String username) throws DataAccessException;
    public void createUser(User user) throws DataAccessException;
}
