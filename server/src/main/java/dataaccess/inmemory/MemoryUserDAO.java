package dataaccess.inmemory;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements dataaccess.UserDAO {
    final private HashMap<String, UserData> users = new HashMap<>();
    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public void createUser(String username, String password, String email) {
        users.put(username, new UserData(username, password, email));
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }
}
