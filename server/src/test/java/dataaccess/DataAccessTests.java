package dataaccess;

import dataaccess.mysql.*;
import dataaccess.inmemory.*;
import exception.ExceptionResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import model.*;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {
  private AuthDAO getAuthDAO(Class<? extends AuthDAO> authDAOClass) throws ExceptionResult {
    AuthDAO authDAO;
    if (authDAOClass.equals(MySQLAuthDAO.class)) {
      authDAO = new MySQLAuthDAO();
    } else {
      authDAO = new MemoryAuthDAO();
    }
    authDAO.clear();
    return authDAO;
  }

  private UserDAO getUserDAO(Class<? extends UserDAO> userDAOClass) throws ExceptionResult {
    UserDAO userDAO;
    if (userDAOClass.equals(MySQLUserDAO.class)) {
      userDAO = new MySQLUserDAO();
    } else {
      userDAO = new MemoryUserDAO();
    }
    userDAO.clear();
    return userDAO;
  }

  private GameDAO getGameDAO(Class<? extends GameDAO> gameDAOClass) throws ExceptionResult {
    GameDAO gameDAO;
    if (gameDAOClass.equals(MySQLGameDAO.class)) {
      gameDAO = new MySQLGameDAO();
    } else {
      gameDAO = new MemoryGameDAO();
    }
    gameDAO.clear();
    return gameDAO;
  }

  @ParameterizedTest
  @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
  @DisplayName("Create Auth Success")
  void createAuthSuccess(Class<? extends AuthDAO> authDAOClass) throws ExceptionResult {
    AuthDAO authDAO = getAuthDAO(authDAOClass);
    assertDoesNotThrow(() -> assertInstanceOf(AuthData.class,authDAO.createAuth("testUsername")));
  }
  @ParameterizedTest
  @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
  @DisplayName("Null username at create auth")
  void createAuthFailure(Class<? extends AuthDAO> authDAOClass) throws ExceptionResult {
    AuthDAO authDAO = getAuthDAO(authDAOClass);
    assertThrows(ExceptionResult.class, () -> authDAO.createAuth(null));
  }




  @ParameterizedTest
  @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
  void authClear(Class<? extends AuthDAO> authDAOClass) throws ExceptionResult {
    AuthDAO authDAO = getAuthDAO(authDAOClass);
    assertDoesNotThrow(authDAO::clear);
  }

}
