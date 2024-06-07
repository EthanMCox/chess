package client;

import exception.ExceptionResult;
import org.junit.jupiter.api.*;
import results.*;
import server.Server;
import serverclientcommunication.ServerFacade;
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


}
