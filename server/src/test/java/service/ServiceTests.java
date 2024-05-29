package service;

import chess.ChessGame;
import dataaccess.inmemory.*;
import dataaccess.mysql.*;
import exception.ExceptionResult;
import model.*;
import dataaccess.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import requests.*;
import results.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

  private static UserService userService;
  private static GameService gameService;
  private static ClearService clearService;

  @BeforeAll
  static void setUp() {
    AuthDAO authDAO = new MySQLAuthDAO();
    UserDAO userDAO = new MySQLUserDAO();
    GameDAO gameDAO = new MySQLGameDAO();
    try {
      DatabaseManager.configureDataBase();
    } catch (ExceptionResult | DataAccessException ex) {
      authDAO = new MemoryAuthDAO();
      userDAO = new MemoryUserDAO();
      gameDAO = new MemoryGameDAO();
    }
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

  @Test
  @DisplayName("Valid login")
  void loginValid() throws ExceptionResult {
    registerValid();
    LoginRequest request = new LoginRequest("testUser", "1234");
    LoginResult expected = new LoginResult("testUser", "authToken");
    LoginResult actual = userService.login(request);
    assertEquals(expected.username(), actual.username());
    assertNotNull(actual.authToken());
    assertInstanceOf(LoginResult.class, actual);
  }

  @Test
  @DisplayName("invalid password at login")
  void loginInvalidPassword() throws ExceptionResult {
    registerValid();
    LoginRequest request = new LoginRequest("testUser", "5678");
    ExceptionResult actual = assertThrows(ExceptionResult.class, () -> userService.login(request));
    ExceptionResult expected = new ExceptionResult(401, "Error: unauthorized");
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Valid logout")
  void logoutValid() throws ExceptionResult {
    RegisterRequest registerRequest = new RegisterRequest("testUser", "1234", "someone@byu.edu");
    LoginResult loginResult = userService.register(registerRequest);
    AuthRequest request = new AuthRequest(loginResult.authToken());
    SuccessResult expected = new SuccessResult();
    SuccessResult actual = userService.logout(request);
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Wrong authToken at logout")
  void logoutBadAuthToken() throws ExceptionResult {
    AuthRequest request = new AuthRequest("badAuthToken");
    ExceptionResult actual = assertThrows(ExceptionResult.class, () -> userService.logout(request));
    ExceptionResult expected = new ExceptionResult(401, "Error: unauthorized");
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Valid game creation")
  void createGameValid() throws ExceptionResult {
    RegisterRequest registerRequest = new RegisterRequest("testUser", "1234", "someone@byu.edu");
    LoginResult loginResult = userService.register(registerRequest);
    CreateGameRequest request = new CreateGameRequest("testGame", loginResult.authToken());
    CreateGameResult expected = new CreateGameResult(1);
    CreateGameResult actual = gameService.createGame(request);
    assertEquals(expected.gameID(), actual.gameID());
    assertInstanceOf(CreateGameResult.class, actual);
  }

  @Test
  @DisplayName("null gameName at createGame")
  void createGameNullGameName() throws ExceptionResult {
    RegisterRequest registerRequest = new RegisterRequest("testUser", "1234", "someone@byu.edu");
    LoginResult loginResult = userService.register(registerRequest);
    CreateGameRequest request = new CreateGameRequest(null, loginResult.authToken());
    ExceptionResult actual = assertThrows(ExceptionResult.class, () -> gameService.createGame(request));
    ExceptionResult expected = new ExceptionResult(400, "Error: bad request");
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("null authToken at createGame")
  void createGameNullAuthToken() throws ExceptionResult {
    CreateGameRequest request = new CreateGameRequest("testGame", null);
    ExceptionResult actual = assertThrows(ExceptionResult.class, () -> gameService.createGame(request));
    ExceptionResult expected = new ExceptionResult(400, "Error: bad request");
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Valid game listing")
  void listGamesValid() throws ExceptionResult {
    RegisterRequest registerRequest = new RegisterRequest("testUser", "1234", "someone@byu.edu");
    LoginResult loginResult = userService.register(registerRequest);
    CreateGameRequest createGameRequest = new CreateGameRequest("testGame", loginResult.authToken());
    gameService.createGame(createGameRequest);
    AuthRequest request = new AuthRequest(loginResult.authToken());
    Collection<ListGamesData> gameList = new HashSet<>();
    gameList.add(new ListGamesData(1, null, null, "testGame"));
    ListGamesResult expected = new ListGamesResult(gameList);
    ListGamesResult actual = gameService.listGames(request);
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("null authToken at listGames")
  void listGamesNullAuthToken() {
    AuthRequest request = new AuthRequest(null);
    ExceptionResult actual = assertThrows(ExceptionResult.class, () -> gameService.listGames(request));
    ExceptionResult expected = new ExceptionResult(400, "Error: bad request");
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Wrong authToken at ListGames")
  void listGamesBadAuthToken() throws ExceptionResult {
    AuthRequest request = new AuthRequest("badAuthToken");
    ExceptionResult actual = assertThrows(ExceptionResult.class, () -> gameService.listGames(request));
    ExceptionResult expected = new ExceptionResult(401, "Error: unauthorized");
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Valid game joining")
  void joinGameValid() throws ExceptionResult {
    RegisterRequest registerRequest = new RegisterRequest("testUser", "1234", "someone@byu.edu");
    LoginResult loginResult = userService.register(registerRequest);
    CreateGameRequest createGameRequest = new CreateGameRequest("testGame", loginResult.authToken());
    CreateGameResult createGameResult = gameService.createGame(createGameRequest);
    JoinGameRequest request = new JoinGameRequest(loginResult.authToken(), ChessGame.TeamColor.WHITE, createGameResult.gameID());
    SuccessResult expected = new SuccessResult();
    SuccessResult actual = gameService.joinGame(request);
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("null playerColor at joinGame")
  void joinGameNullPlayerColor() throws ExceptionResult {
    RegisterRequest registerRequest = new RegisterRequest("testUser", "1234", "someone@byu.edu");
    LoginResult loginResult = userService.register(registerRequest);
    CreateGameRequest createGameRequest = new CreateGameRequest("testGame", loginResult.authToken());
    CreateGameResult createGameResult = gameService.createGame(createGameRequest);
    JoinGameRequest request = new JoinGameRequest(loginResult.authToken(), null, createGameResult.gameID());
    ExceptionResult actual = assertThrows(ExceptionResult.class, () -> gameService.joinGame(request));
    ExceptionResult expected = new ExceptionResult(400, "Error: bad request");
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("blackUsername already taken at joinGame")
  void joinGameBlackUsernameTaken() throws ExceptionResult {
    RegisterRequest registerRequest = new RegisterRequest("testUser", "1234", "someone@byu.edu");
    LoginResult loginResult = userService.register(registerRequest);
    CreateGameRequest createGameRequest = new CreateGameRequest("testGame", loginResult.authToken());
    CreateGameResult createGameResult = gameService.createGame(createGameRequest);
    LoginResult secondAuth = userService.register(new RegisterRequest("testUser2", "5678", "anotheremail@byu.edu"));
    gameService.joinGame(new JoinGameRequest(secondAuth.authToken(), ChessGame.TeamColor.BLACK, createGameResult.gameID()));
    JoinGameRequest request = new JoinGameRequest(loginResult.authToken(), ChessGame.TeamColor.BLACK, createGameResult.gameID());
    ExceptionResult actual = assertThrows(ExceptionResult.class, () -> gameService.joinGame(request));
    ExceptionResult expected = new ExceptionResult(403, "Error: already taken");
    assertEquals(expected, actual);
  }
}



