package client;

import chess.ChessGame;
import exception.ExceptionResult;
import model.ListGamesData;
import org.junit.jupiter.api.*;
import results.*;
import server.Server;
import serverclientcommunication.ServerFacade;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        int port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    @Test
    @DisplayName("clear database")
    public void clearDatabase() throws ExceptionResult {
        assertDoesNotThrow(() -> assertInstanceOf(SuccessResult.class, facade.clear()));
    }


    @Test
    @DisplayName("register test")
    public void registerSuccess() throws ExceptionResult {
        LoginResult actual = assertDoesNotThrow(() -> facade.register("testUser", "password123", "email@email.com"));
        LoginResult expected = new LoginResult("testUser", "authToken");
        Assertions.assertEquals(expected.username(), actual.username());
        Assertions.assertNotNull(actual.authToken());
    }

    @Test
    @DisplayName("Tried to register same user twice")
    public void registerTwice() throws ExceptionResult {
        assertDoesNotThrow(() -> facade.register("testUser", "password123", "email@email.com"));
        ExceptionResult ex = assertThrows(ExceptionResult.class, () -> facade.register("testUser", "password123", "email@email.com"));
        assertEquals(403, ex.statusCode());
    }

    @Test
    @DisplayName("logout success")
    public void logoutSuccess() throws ExceptionResult {
        LoginResult loginResult = assertDoesNotThrow(() -> facade.register("testUser", "password123", "email@email.com"));
        assertDoesNotThrow(() -> assertInstanceOf(SuccessResult.class, facade.logout(loginResult.authToken())));
    }

    @Test
    @DisplayName("logout with invalid auth")
    public void logoutInvalidAuth() throws ExceptionResult {
        assertDoesNotThrow(() -> facade.register("testUser", "password123", "email@email.com"));
        ExceptionResult ex = assertThrows(ExceptionResult.class, () -> facade.logout("invalidAuth"));
        assertEquals(401, ex.statusCode());
    }

    @Test
    @DisplayName("Valid login")
    public void loginSuccess() throws ExceptionResult {
        LoginResult registerResult = assertDoesNotThrow(() -> facade.register("testUser", "password123", "email@email.com"));
        facade.logout(registerResult.authToken());
        LoginResult actual = assertDoesNotThrow(() -> facade.login("testUser", "password123"));
        LoginResult expected = new LoginResult("testUser", "authToken");
        assertEquals(expected.username(), actual.username());
    }

    @Test
    @DisplayName("login with invalid password")
    public void loginInvalidPassword() throws ExceptionResult {
        assertDoesNotThrow(() -> facade.register("testUser", "password123", "email@email.com"));
        ExceptionResult ex = assertThrows(ExceptionResult.class, () -> facade.login("testUser", "invalidPassword"));
        assertEquals(401, ex.statusCode());
    }

    @Test
    @DisplayName("Create game success")
    public void createGameSuccess() throws ExceptionResult {
        LoginResult loginResult = assertDoesNotThrow(() -> facade.register("testUser", "password123", "email@email.com"));
        CreateGameResult actual = assertDoesNotThrow(() -> facade.createGame("testGame", loginResult.authToken()));
        CreateGameResult expected = new CreateGameResult(1);
        assertEquals(expected.gameID(), actual.gameID());
    }

    @Test
    @DisplayName("Create game with invalid auth")
    public void createGameInvalidAuth() throws ExceptionResult {
        ExceptionResult ex = assertThrows(ExceptionResult.class, () -> facade.createGame("testGame", "invalidAuth"));
        assertEquals(401, ex.statusCode());
    }

    @Test
    @DisplayName("List games success")
    public void listGamesSuccess() throws ExceptionResult {
        LoginResult loginResult = assertDoesNotThrow(() -> facade.register("testUser", "password123", "email@email.com"));
        CreateGameResult createGameResult = assertDoesNotThrow(() -> facade.createGame("testGame", loginResult.authToken()));
        facade.createGame("testGame2", loginResult.authToken());
        ListGamesResult actual = assertDoesNotThrow(() -> facade.listGames(loginResult.authToken()));
        Collection<ListGamesData> expectedGames = new HashSet<>();
        expectedGames.add(new ListGamesData(1, null, null, "testGame"));
        expectedGames.add(new ListGamesData(2, null, null, "testGame2"));
        ListGamesResult expected = new ListGamesResult(expectedGames);
        assertEquals(new HashSet<>(expected.games()), new HashSet<>(actual.games()));
    }

    @Test
    @DisplayName("List games with invalid auth")
    public void listGamesInvalidAuth() throws ExceptionResult {
        ExceptionResult ex = assertThrows(ExceptionResult.class, () -> facade.listGames("invalidAuth"));
        assertEquals(401, ex.statusCode());
    }

    @Test
    @DisplayName("Join game success")
    public void joinGameSuccess() throws ExceptionResult {
        LoginResult loginResult = assertDoesNotThrow(() -> facade.register("testUser", "password123", "email@email.com"));
        assertDoesNotThrow(() -> facade.createGame("testGame", loginResult.authToken()));
        SuccessResult actual = assertDoesNotThrow(() -> facade.joinGame(1, ChessGame.TeamColor.WHITE, loginResult.authToken()));
        assertInstanceOf(SuccessResult.class, actual);
    }

    @Test
    @DisplayName("Join game as white but white is already taken")
    public void joinGameWhiteTaken() throws ExceptionResult {
        LoginResult loginResult = assertDoesNotThrow(() -> facade.register("testUser", "password123", "email@email.com"));
        assertDoesNotThrow(() -> facade.createGame("testGame", loginResult.authToken()));
        assertDoesNotThrow(() -> facade.joinGame(1, ChessGame.TeamColor.WHITE, loginResult.authToken()));
        ExceptionResult ex = assertThrows(ExceptionResult.class, () -> facade.joinGame(1, ChessGame.TeamColor.WHITE, loginResult.authToken()));
        assertEquals(403, ex.statusCode());
    }
}
