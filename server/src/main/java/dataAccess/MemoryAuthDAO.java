package dataAccess;

import model.*;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    final private HashMap<String, Auth> authUsers = new HashMap<>();
    public MemoryAuthDAO() {}

    public String createAuth(String username) {
        // apply a random string generator to the auth token
        String authToken = UUID.randomUUID().toString();
        Auth myAuth = new Auth(username, authToken);
        authUsers.put(username, myAuth);
        return myAuth.getAuthToken();
    }
}
