package dataAccess.userDAOs;

import DataAccessException.DataAccessException;
import model.*;

public interface UserDAO {
    User getUser(String username) throws DataAccessException;
    void createUser(User user) throws DataAccessException;
    void clear();

}
