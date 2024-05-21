package server;

import exception.ExceptionResult;
import spark.*;
import util.JsonSerializer;

import java.util.Map;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createRoutes();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void createRoutes() {
      Spark.delete("/db", this::clear);
      Spark.post("/user", this::registerUser);
      Spark.post("session", this::loginUser);
      Spark.delete("session", this::logoutUser);
      Spark.get("/game", this::listGames);
      Spark.post("/game", this::createGame);
      Spark.post("/game", this::joinGame);

      Spark.exception(ExceptionResult.class,this::exceptionHandler);
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
