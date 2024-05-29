package dataaccess.mysql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import exception.ExceptionResult;
import model.AuthData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLAuthDAO implements AuthDAO {

  public MySQLAuthDAO() {
  }

  @Override
  public void clear() throws ExceptionResult {
    var statement = "TRUNCATE TABLE auth";
  }

  @Override
  public AuthData createAuth(String username) throws ExceptionResult {
    return null;
  }

  @Override
  public AuthData getAuth(String authToken) throws ExceptionResult {
    return null;
  }

  @Override
  public void deleteAuth(AuthData authData) throws ExceptionResult {

  }

  private int executeUpdate(String statement, Object... params) throws ExceptionResult {
    try (var conn = DatabaseManager.getConnection()) {
      try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
        for (var i = 0; i < params.length; i++) {
          var param = params[i];
          if (param instanceof String p) ps.setString(i + 1, p);
          else if (param instanceof Integer p) ps.setInt(i + 1, p);
          else if (param == null) ps.setNull(i + 1, NULL);
        }
        ps.executeUpdate();

        var rs = ps.getGeneratedKeys();
        if (rs.next()) {
          return rs.getInt(1);
        }

        return 0;
      }
    } catch (SQLException | DataAccessException e) {
      throw new ExceptionResult(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
    }
  }


}
