package dataaccess.mysql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import exception.ExceptionResult;
import model.AuthData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLAuthDAO extends SQLUpdateExecutor implements AuthDAO {

  public MySQLAuthDAO() {
  }

  @Override
  public void clear() throws ExceptionResult {
    var statement = "TRUNCATE TABLE auth";
    executeUpdate(statement);
  }

  @Override
  public AuthData createAuth(String username) throws ExceptionResult {
      var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
      var authToken = generateAuthToken();

      executeUpdate(statement, username, authToken);
      return new AuthData(authToken, username);
  }

  private AuthData getAuthUser(String username) throws ExceptionResult {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT username, authToken FROM auth WHERE username=?";
      try (var stmt = conn.prepareStatement(statement)) {
        stmt.setString(1, username);
        try (var rs = stmt.executeQuery()) {
          if (rs.next()) {
            return new AuthData(rs.getString("authToken"), username);
          } else {
            return null;
          }
        }
      }
    } catch (SQLException | DataAccessException e) {
      throw new ExceptionResult(500, String.format("unable to read database: %s", e.getMessage()));
    }
  }

  @Override
  public AuthData getAuth(String authToken) throws ExceptionResult {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT username, authToken FROM auth WHERE authToken=?";
      try (var stmt = conn.prepareStatement(statement)) {
        stmt.setString(1, authToken);
        try (var rs = stmt.executeQuery()) {
          if (rs.next()) {
            return new AuthData(authToken, rs.getString("username"));
          } else {
            return null;
          }
        }
      }
    } catch (SQLException | DataAccessException e) {
      throw new ExceptionResult(500, String.format("unable to read database: %s", e.getMessage()));
    }
  }

  @Override
  public void deleteAuth(AuthData authData) throws ExceptionResult {
    var statement = "DELETE FROM auth WHERE authToken=?";
    if (authData == null) throw new ExceptionResult(400, "bad request");
    executeUpdate(statement, authData.authToken());
  }
}
