package dataaccess.inMemory;

import dataaccess.AuthDAO;
import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    final private HashMap<String, AuthData> auths = new HashMap<>();
    @Override
    public void clear() {
        auths.clear();
    }

    @Override
    public AuthData createAuth(String username) {
        String authToken = generateAuthToken();
        return new AuthData(authToken, username);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }
}
