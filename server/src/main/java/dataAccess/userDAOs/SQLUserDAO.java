package dataAccess.userDAOs;

import dataAccess.DataAccessException;
import model.User;

public class SQLUserDAO implements UserDAO {
    @Override
    public User getUser(String username) throws DataAccessException {
        return new User("null", "null","null");
    }
    @Override
    public void createUser(User user) throws DataAccessException {
    }
    @Override
    public void clear(){

    }
}
