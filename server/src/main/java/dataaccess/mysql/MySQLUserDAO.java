package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import exception.ExceptionResult;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;

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
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT username, password, email FROM user WHERE username=?";
      try (var stmt = conn.prepareStatement(statement)) {
        stmt.setString(1, username);
        try (var rs = stmt.executeQuery()) {
          if (rs.next()) {
            return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
          } else {
            return null;
          }
        }
      }
    } catch (SQLException | DataAccessException e) {
      throw new ExceptionResult(500, String.format("unable to read database: %s", e.getMessage()));
    }
  }
}
