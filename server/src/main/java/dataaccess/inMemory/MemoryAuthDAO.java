package dataaccess.inMemory;

import dataaccess.AuthDAO;
import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    @Override
    public void clear() {
    }

    @Override
    public AuthData createAuth(String username) {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
    }

    @Override
    public String generateAuthToken() {
        return null;
    }
}
