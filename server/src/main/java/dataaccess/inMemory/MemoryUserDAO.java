package dataaccess.inMemory;

import model.UserData;
public class MemoryUserDAO implements dataaccess.UserDAO {
    @Override
    public void clear() {
    }

    @Override
    public void createUser(String username, String password, String email) {
    }

    @Override
    public UserData getUser(String username) {
        return null;
    }
}
