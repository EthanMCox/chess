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
        Spark.delete("/db", (req, res) -> { // clear() endpoint
            return "Placeholder";
        });
        Spark.post("/user", (req, res) -> { // register() endpoint
            return "Placeholder";
        });
        Spark.post("session", (req, res) -> { // login() endpoint
            return "Placeholder";
        });
        Spark.delete("session", (req, res) -> { // logout() endpoint
            return "Placeholder";
        });
        Spark.get("/game", (req, res) -> { // listGames() endpoint
            return "Placeholder";
        });
        Spark.post("/game", (req, res) -> { // createGame() endpoint
            return "Placeholder";
        });
        Spark.post("/game", (req, res) -> { // joinGame() endpoint
            return "Placeholder";
        });
        Spark.notFound("<html><body><h1>404 Not Found</h1></body></html>");
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
