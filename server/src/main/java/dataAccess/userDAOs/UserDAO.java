package dataAccess.userDAOs;

import dataAccess.DataAccessException;
import model.*;

public interface UserDAO {
    User getUser(String username) throws DataAccessException;
    void createUser(User user) throws DataAccessException;
    void clear();

}
