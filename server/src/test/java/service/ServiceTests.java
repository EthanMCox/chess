package service;

import dataaccess.inmemory.*;
import exception.ExceptionResult;
import model.*;
import dataaccess.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
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
}
