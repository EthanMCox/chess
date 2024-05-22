package dataaccess;

import exception.ExceptionResult;
import model.AuthData;

public interface AuthDAO {
  void clear() throws ExceptionResult;
  AuthData createAuth(String username) throws ExceptionResult;
  AuthData getAuth(String authToken) throws ExceptionResult;
  void deleteAuth(AuthData authData) throws ExceptionResult;
  default String generateAuthToken() {
    return java.util.UUID.randomUUID().toString();
  }
}
