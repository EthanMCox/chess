package dataaccess.mysql;

import dataaccess.UserDAO;
import exception.ExceptionResult;
import model.UserData;

public class MySQLUserDAO implements UserDAO {
  @Override
  public void clear() throws ExceptionResult {

  }

  @Override
  public void createUser(String username, String password, String email) throws ExceptionResult {

  }

  @Override
  public UserData getUser(String username) throws ExceptionResult {
    return null;
  }
}
