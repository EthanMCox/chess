package server;

import dataaccess.inMemory.*;
import dataaccess.*;
import exception.ExceptionResult;
import requests.*;
import results.*;
import results.SuccessResult;
import service.*;
import spark.*;
import util.JsonSerializer;

import java.util.Map;

public class Server {
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;

    public Server() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(gameDAO, authDAO);
        this.clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    public Server(UserService userService, GameService gameService, ClearService clearService) {
        this.userService = userService;
        this.gameService = gameService;
        this.clearService = clearService;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        createRoutes();

        Spark.exception(ExceptionResult.class,this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public int port() {
        return Spark.port();
    }

    private void createRoutes() {
      Spark.delete("/db", this::clear);
      Spark.post("/user", this::registerUser);
      Spark.post("/session", this::loginUser);
      Spark.delete("/session", this::logoutUser);
      Spark.get("/game", this::listGames);
      Spark.post("/game", this::createGame);
      Spark.put("/game", this::joinGame);
      Spark.notFound("<html><body><h1>404 Error: Not Found</h1></body></html>");
    }

    private Object clear(Request req, Response res) throws ExceptionResult{
        SuccessResult result = clearService.clearAll();
        return JsonSerializer.serialize(result);
    }

    private Object registerUser(Request req, Response res) throws ExceptionResult{
        RegisterRequest request = JsonSerializer.deserialize(req.body(), RegisterRequest.class);
        LoginResult result = userService.register(request);
        return JsonSerializer.serialize(result);
    }

    private Object loginUser(Request req, Response res) throws ExceptionResult{
        LoginRequest request = JsonSerializer.deserialize(req.body(), LoginRequest.class);
        LoginResult result = userService.login(request);
        return JsonSerializer.serialize(result);
    }

    private Object logoutUser(Request req, Response res) throws ExceptionResult{
        String authToken = req.headers("Authorization");
        AuthRequest request = JsonSerializer.deserialize(req.body(), AuthRequest.class);
        request = request.setAuthToken(authToken);
        SuccessResult result = userService.logout(request);
        return JsonSerializer.serialize(result);
    }

    private Object listGames(Request req, Response res) throws ExceptionResult{
        String authToken = req.headers("Authorization");
        AuthRequest request = JsonSerializer.deserialize(req.body(), AuthRequest.class);
        request = request.setAuthToken(authToken);
        ListGamesResult result = gameService.listGames(request);
        return JsonSerializer.serialize(result);
    }

    private Object createGame(Request req, Response res) throws ExceptionResult{
        String authToken = req.headers("Authorization");
        CreateGameRequest request = JsonSerializer.deserialize(req.body(), CreateGameRequest.class);
        request = request.setAuthToken(authToken);
        CreateGameResult result = gameService.createGame(request);
        return JsonSerializer.serialize(result);
    }

    private Object joinGame(Request req, Response res) throws ExceptionResult{
        String authToken = req.headers("Authorization");
        JoinGameRequest request = JsonSerializer.deserialize(req.body(), JoinGameRequest.class);
        request = request.setAuthToken(authToken);
        SuccessResult result = gameService.joinGame(request);
        return JsonSerializer.serialize(result);
    }

    private void exceptionHandler(ExceptionResult ex, Request req, Response res) {
        res.status(ex.StatusCode());
        Map<String, String> errorResponse = Map.of("message", ex.getMessage());
        res.body(JsonSerializer.serialize(errorResponse));
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
