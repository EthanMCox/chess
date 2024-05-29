package dataaccess.mysql;

import dataaccess.AuthDAO;
import exception.ExceptionResult;
import model.AuthData;

public class MySQLAuthDAO implements AuthDAO {

  public MySQLAuthDAO() {
  }

  @Override
  public void clear() throws ExceptionResult {

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


}
