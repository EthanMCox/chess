package dataaccess.inmemory;

import dataaccess.AuthDAO;
import exception.ExceptionResult;
import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    final private HashMap<String, AuthData> auths = new HashMap<>();
    @Override
    public void clear() {
        auths.clear();
    }

    @Override
    public AuthData createAuth(String username) throws ExceptionResult {
        if (username == null) {
            throw new ExceptionResult(400, "Username cannot be null");
        }
        String authToken = generateAuthToken();
        auths.put(authToken, new AuthData(authToken, username));
        return new AuthData(authToken, username);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }

    @Override
    public void deleteAuth(AuthData authData) throws ExceptionResult {
        if (authData == null) {
            throw new ExceptionResult(400, "bad request");
        }
        auths.remove(authData.authToken());
    }
}
