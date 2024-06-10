package server;


import dataaccess.*;
import dataaccess.inmemory.*;
import dataaccess.mysql.*;
import exception.ExceptionResult;
import requests.*;
import results.*;
import results.SuccessResult;
import service.*;
import spark.*;
import util.JsonSerializer;
import server.websocket.WebSocketHandler;


import java.util.Map;

public class Server {
    private UserService userService;
    private GameService gameService;
    private ClearService clearService;
    private WebsocketService websocketService;
    private WebSocketHandler webSocketHandler;

    public Server() {
        try {
            setUpServices();
        } catch (ExceptionResult | DataAccessException e) {
          throw new RuntimeException(e);
        }
    }

    public Server(UserService userService, GameService gameService, ClearService clearService, WebsocketService websocketService) {
        this.userService = userService;
        this.gameService = gameService;
        this.clearService = clearService;
        this.websocketService = websocketService;
        this.webSocketHandler = new WebSocketHandler(websocketService);
    }

    private void setUpServices() throws ExceptionResult, DataAccessException {
        AuthDAO authDAO;
        UserDAO userDAO;
        GameDAO gameDAO;
        try {
            authDAO = new MySQLAuthDAO();
            userDAO = new MySQLUserDAO();
            gameDAO = new MySQLGameDAO();
            DatabaseManager.configureDataBase();
        } catch (ExceptionResult | DataAccessException ex) {
            authDAO = new MemoryAuthDAO();
            userDAO = new MemoryUserDAO();
            gameDAO = new MemoryGameDAO();
        }
        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(gameDAO, authDAO);
        this.clearService = new ClearService(userDAO, gameDAO, authDAO);
        this.websocketService = new WebsocketService(userDAO, gameDAO, authDAO);
        this.webSocketHandler = new WebSocketHandler(websocketService);
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
      Spark.webSocket("/connect", webSocketHandler);
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
        String authToken = req.headers("authorization");
        AuthRequest request = new AuthRequest(authToken);
        SuccessResult result = userService.logout(request);
        return JsonSerializer.serialize(result);
    }

    private Object listGames(Request req, Response res) throws ExceptionResult{
        String authToken = req.headers("authorization");
        AuthRequest request = new AuthRequest(authToken);
        ListGamesResult result = gameService.listGames(request);
        return JsonSerializer.serialize(result);
    }

    private Object createGame(Request req, Response res) throws ExceptionResult{
        String authToken = req.headers("authorization");
        CreateGameRequest request = JsonSerializer.deserialize(req.body(), CreateGameRequest.class);
        request = request.setAuthToken(authToken);
        CreateGameResult result = gameService.createGame(request);
        return JsonSerializer.serialize(result);
    }

    private Object joinGame(Request req, Response res) throws ExceptionResult{
        String authToken = req.headers("authorization");
        JoinGameRequest request = JsonSerializer.deserialize(req.body(), JoinGameRequest.class);
        request = request.setAuthToken(authToken);
        SuccessResult result = gameService.joinGame(request);
        return JsonSerializer.serialize(result);
    }

    private void exceptionHandler(ExceptionResult ex, Request req, Response res) {
        res.status(ex.statusCode());
        Map<String, String> errorResponse = Map.of("message", ex.getMessage());
        res.body(JsonSerializer.serialize(errorResponse));
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
