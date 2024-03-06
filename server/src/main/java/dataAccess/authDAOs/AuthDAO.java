package dataAccess.authDAOs;

import dataAccess.DataAccessException;
import model.*;

public interface AuthDAO {
    Auth createAuth(String username) throws DataAccessException;
    void deleteAuth(String authToken);
    Auth getAuth(String authToken);
    void clear();
}