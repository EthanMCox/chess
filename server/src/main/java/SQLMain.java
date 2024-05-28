import dataaccess.*;
import dataaccess.mysql.*;
import service.*;
import server.Server;

public class SQLMain {

  public static void main(String[] args) {
    try {
      int port = 8080;
      if (args.length >= 1) {
        port = Integer.parseInt(args[0]);
      }
      var userDAO = new MySQLUserDAO();
      var gameDAO = new MySQLGameDAO();
      var authDAO = new MySQLAuthDAO();
      var clearService = new ClearService(userDAO, gameDAO, authDAO);
      var gameService = new GameService(gameDAO, authDAO);
      var userService = new UserService(userDAO, authDAO);
      var server = new Server(userService, gameService, clearService);
      port = server.run(port);
      System.out.printf("Server started on port %d%n", port);
    } catch (Throwable ex) {
      System.out.printf("Unable to start server: %s%n", ex.getMessage());
    }
  }
}