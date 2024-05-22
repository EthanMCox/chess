package server;

import exception.ExceptionResult;
import service.*;
import spark.*;
import util.JsonSerializer;

import java.util.Map;

public class Server {
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;

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
        return "Placeholder";
    }

    private Object registerUser(Request req, Response res) throws ExceptionResult{
        return "Placeholder";
    }

    private Object loginUser(Request req, Response res) throws ExceptionResult{
        return "Placeholder";
    }

    private Object logoutUser(Request req, Response res) throws ExceptionResult{
        return "Placeholder";
    }

    private Object listGames(Request req, Response res) throws ExceptionResult{
        return "Placeholder";
    }

    private Object createGame(Request req, Response res) throws ExceptionResult{
        return "Placeholder";
    }

    private Object joinGame(Request req, Response res) throws ExceptionResult{
        return "Placeholder";
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
