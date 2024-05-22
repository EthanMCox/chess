package dataaccess;

import exception.ExceptionResult;
import model.UserData;

public interface UserDAO {
  void clear() throws ExceptionResult;
  void createUser(String username, String password, String email) throws ExceptionResult;
  UserData getUser(String username) throws ExceptionResult;
}
