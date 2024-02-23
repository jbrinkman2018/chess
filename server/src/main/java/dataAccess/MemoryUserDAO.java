package dataAccess;

import java.util.HashMap;
import model.*;

public class MemoryUserDAO implements UserDAO{
    final private HashMap<String, User> users = new HashMap<>();
    public MemoryUserDAO() {}

    public User getUser(String username) {
        return users.get(username);
    }
    public void createUser(User user) {
        users.put(user.getUsername(),user);
    }
}
