package dataAccess.userDAOs;

import java.util.HashMap;
import model.*;

public class MemoryUserDAO implements UserDAO{
    final private HashMap<String, User> users = new HashMap<>();
    @Override
    public User getUser(String username) {
        return users.get(username);
    }
    @Override
    public void createUser(User user) {
        users.put(user.getUsername(),user);
    }
    @Override
    public void clear(){
        users.clear();
    }
}
