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
  @DisplayName("authClear succeeds")
  void authClear(Class<? extends AuthDAO> authDAOClass) throws ExceptionResult {
    AuthDAO authDAO = getAuthDAO(authDAOClass);
    assertDoesNotThrow(authDAO::clear);
  }

  @ParameterizedTest
  @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
  @DisplayName("Get auth succeeds")
  void getAuthSuccess(Class<? extends AuthDAO> authDAOClass) throws ExceptionResult {
    AuthDAO authDAO = getAuthDAO(authDAOClass);
    AuthData authData = authDAO.createAuth("testUsername");
    assertDoesNotThrow(() -> assertInstanceOf(AuthData.class, authDAO.getAuth(authData.authToken())));
  }

  @ParameterizedTest
  @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
  @DisplayName("No matching auth found")
  void getAuthDoesNotMatch(Class<? extends AuthDAO> authDAOClass) throws ExceptionResult {
    AuthDAO authDAO = getAuthDAO(authDAOClass);
    assertDoesNotThrow(() -> assertNull(authDAO.getAuth("testAuthToken")));
  }

  @ParameterizedTest
  @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
  @DisplayName("Delete Auth Success")
  void deleteAuthSuccess(Class<? extends AuthDAO> authDAOClass) throws ExceptionResult {
    AuthDAO authDAO = getAuthDAO(authDAOClass);
    AuthData authData = authDAO.createAuth("testUsername");
    assertDoesNotThrow(() -> authDAO.deleteAuth(authData));
  }

  @ParameterizedTest
  @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
  @DisplayName("Delete Auth Fails because authData is null")
  void deleteAuthFailure(Class<? extends AuthDAO> authDAOClass) throws ExceptionResult {
    AuthDAO authDAO = getAuthDAO(authDAOClass);
    assertThrows(ExceptionResult.class, () -> authDAO.deleteAuth(null));
  }

  @ParameterizedTest
  @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
  @DisplayName("Clear User Success")
  void clearUserSuccess(Class<? extends UserDAO> userDAOClass) throws ExceptionResult {
    UserDAO userDAO = getUserDAO(userDAOClass);
    assertDoesNotThrow(userDAO::clear);
  }

  @ParameterizedTest
  @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
  @DisplayName("Create User Success")
  void createUserSuccess(Class<? extends UserDAO> userDAOClass) throws ExceptionResult {
    UserDAO userDAO = getUserDAO(userDAOClass);
    assertDoesNotThrow(() -> userDAO.createUser("testUsername", "testPassword", "testEmail"));
  }

  @ParameterizedTest
  @DisplayName("password value is null when creating user")
  @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
  void createUserFailure(Class<? extends UserDAO> userDAOClass) throws ExceptionResult {
    UserDAO userDAO = getUserDAO(userDAOClass);
    assertThrows(ExceptionResult.class, () -> userDAO.createUser("testUsername", null, "testEmail"));
  }

  @ParameterizedTest
  @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
  @DisplayName("Get user Success")
  void getUserSuccess(Class<? extends UserDAO> userDAOClass) throws ExceptionResult {
    UserDAO userDAO = getUserDAO(userDAOClass);
    userDAO.createUser("testUsername", "testPassword", "testEmail");
    assertDoesNotThrow(() -> assertInstanceOf(UserData.class, userDAO.getUser("testUsername")));
    UserData userData = userDAO.getUser("testUsername");
    assertEquals("testUsername", userData.username());
    assertEquals("testPassword", userData.password());
    assertEquals("testEmail", userData.email());
  }

  @ParameterizedTest
  @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
  @DisplayName("No matching user found")
  void getUserDoesNotMatch(Class<? extends UserDAO> userDAOClass) throws ExceptionResult {
    UserDAO userDAO = getUserDAO(userDAOClass);
    assertDoesNotThrow(() -> assertNull(userDAO.getUser("testUsername")));
  }
}
