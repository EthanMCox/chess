package dataaccess;

import model.AuthData;

interface AuthDAO {
  void clear();
  AuthData createAuth(String username);
  AuthData getAuth(String authToken);
  void deleteAuth(String authToken);
  default String generateAuthToken() {
    return java.util.UUID.randomUUID().toString();
  }
}
