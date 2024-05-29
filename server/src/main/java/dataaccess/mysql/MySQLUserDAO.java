package dataaccess.mysql;

import dataaccess.UserDAO;
import exception.ExceptionResult;
import model.UserData;

public class MySQLUserDAO extends SQLUpdateExecutor implements UserDAO  {
  @Override
  public void clear() throws ExceptionResult {
    var statement = "TRUNCATE TABLE user";
    executeUpdate(statement);
  }

  @Override
  public void createUser(String username, String password, String email) throws ExceptionResult {
    var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
    executeUpdate(statement, username, password, email);
  }

  @Override
  public UserData getUser(String username) throws ExceptionResult {
    return null;
  }
}
