package server;

import spark.*;

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
//        Spark.get("/hello", (req, res) -> "Hello World"); // Example
      Spark.delete("/db", this::clear);
      Spark.post("/user", this::registerUser);
      Spark.post("session", this::loginUser);
      Spark.delete("session", this::logoutUser);
      Spark.get("/game", this::listGames);
      Spark.post("/game", this::createGame);
      Spark.post("/game", this::joinGame);

//      Spark.exception(ResponseException.class,this::exceptionHandler);
      Spark.notFound("<html><body><h1>404 Not Found</h1></body></html>");
    }

    private Object clear(Request req, Response res) {
        return "Placeholder";
    }

    private Object registerUser(Request req, Response res) {
        return "Placeholder";
    }

    private Object loginUser(Request req, Response res) {
        return "Placeholder";
    }

    private Object logoutUser(Request req, Response res) {
        return "Placeholder";
    }

    private Object listGames(Request req, Response res) {
        return "Placeholder";
    }

    private Object createGame(Request req, Response res) {
        return "Placeholder";
    }

    private Object joinGame(Request req, Response res) {
        return "Placeholder";
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
