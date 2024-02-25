package dataAccess.authDAOs;

import model.*;

public interface AuthDAO {
    String createAuth(String username);
    void deleteAuth(String authToken);
    Auth getAuth(String authToken);
    void clear();
}