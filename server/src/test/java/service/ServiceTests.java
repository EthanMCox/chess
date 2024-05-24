package service;

import dataaccess.inmemory.*;
import exception.ExceptionResult;
import model.*;
import dataaccess.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import requests.*;
import results.*;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

  private static UserService userService;
  private static GameService gameService;
  private static ClearService clearService;

  @BeforeAll
  static void setUp() {
    AuthDAO authDAO = new MemoryAuthDAO();
    UserDAO userDAO = new MemoryUserDAO();
    GameDAO gameDAO = new MemoryGameDAO();
    userService = new UserService(userDAO, authDAO);
    gameService = new GameService(gameDAO, authDAO);
    clearService = new ClearService(userDAO, gameDAO, authDAO);
  }

  @BeforeEach
  @Test
  void clear() throws ExceptionResult {
    SuccessResult expected = new SuccessResult();
    SuccessResult actual = clearService.clearAll();
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Valid registration")
  void registerValid() throws ExceptionResult {
    RegisterRequest request = new RegisterRequest("testUser", "1234", "someone@byu.edu");
    LoginResult expected = new LoginResult("testUser", "authToken");
    LoginResult actual = userService.register(request);
    assertEquals(expected.username(), actual.username());
    assertNotNull(actual.authToken());
    assertInstanceOf(LoginResult.class, actual);
  }

  @Test
  @DisplayName("null password at registration")
  void registerNullPassword() {
    RegisterRequest request = new RegisterRequest("testUser", null, "someone@byu.edu");
    ExceptionResult actual = assertThrows(ExceptionResult.class, () -> userService.register(request));
    ExceptionResult expected = new ExceptionResult(400, "Error: bad request");
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("null username at registration")
  void registerNullUsername() {
    RegisterRequest request = new RegisterRequest(null, "1234", "someone@byu.edu");
    ExceptionResult actual = assertThrows(ExceptionResult.class, () -> userService.register(request));
    ExceptionResult expected = new ExceptionResult(400, "Error: bad request");
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("null email at registration")
  void registerNullEmail() {
    RegisterRequest request = new RegisterRequest("testUser", "1234", null);
    ExceptionResult actual = assertThrows(ExceptionResult.class, () -> userService.register(request));
    ExceptionResult expected = new ExceptionResult(400, "Error: bad request");
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Registering a user that already exists")
  void registerAlreadyExists() throws ExceptionResult {
    registerValid();
    RegisterRequest request = new RegisterRequest("testUser", "5678", "someone@byu.edu");
    ExceptionResult actual = assertThrows(ExceptionResult.class, () -> userService.register(request));
    ExceptionResult expected = new ExceptionResult(403, "Error: already taken");
    assertEquals(expected, actual);
  }
}



