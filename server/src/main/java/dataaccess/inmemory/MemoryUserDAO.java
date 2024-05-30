package dataaccess.inmemory;

import exception.ExceptionResult;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements dataaccess.UserDAO {
    final private HashMap<String, UserData> users = new HashMap<>();
    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public void createUser(String username, String password, String email) throws ExceptionResult {
        if (username == null || password == null || email == null) {
            throw new ExceptionResult(400, "bad request");
        }
        users.put(username, new UserData(username, password, email));
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }
}
