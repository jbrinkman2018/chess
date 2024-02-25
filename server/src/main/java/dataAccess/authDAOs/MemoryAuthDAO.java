package dataAccess.authDAOs;

import model.*;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private final HashMap<String, Auth> authUsers = new HashMap<>();

    @Override
    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        Auth myAuth = new Auth(username, authToken);
        authUsers.put(authToken, myAuth);
        return myAuth.getAuthToken();
    }
    @Override
    public void deleteAuth(String authToken){
        // check and throw error if authToken is not valid
        authUsers.remove(authToken);
    }
    @Override
    public Auth getAuth(String authToken){
        // check and throw error if authToken is not valid
        return authUsers.get(authToken);
    }
    @Override
    public void clear(){
        authUsers.clear();
    }
}
