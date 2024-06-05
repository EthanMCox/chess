package ui;

import exception.ExceptionResult;
import server.ServerFacade;

import java.util.Arrays;

public class Client {
  private String visitorName = null;
  private final ServerFacade server;
  private final String serverUrl;
  private State state = State.SIGNEDOUT;

  public Client(String serverUrl) {
    this.serverUrl = serverUrl;
    this.server = new ServerFacade();
  }

  public String eval(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "register" -> register(params);
        case "login" -> signIn(params);
        case "logout" -> signOut();
        case "create" -> createGame(params);
        case "list" -> listGames();
        case "join" -> joinGame(params);
        case "observe" -> observeGame(params);
        case "quit" -> "quit";
        default -> help();
      };
    } catch (ExceptionResult ex) {
      return ex.getMessage();
    }
  }

  public String register(String... params) throws ExceptionResult {
    return "placeholder";
  }

  public String signIn(String... params) throws ExceptionResult {
    return "placeholder";
  }

  public String signOut() {
    return "placeholder";
  }

  public String createGame(String... params) throws ExceptionResult {
    return "placeholder";
  }

  public String listGames() throws ExceptionResult {
    return "placeholder";
  }

  public String joinGame(String... params) throws ExceptionResult {
    return "placeholder";
  }

  public String observeGame(String... params) throws ExceptionResult {
    return "placeholder";
  }

  public String help() {
    if (state == State.SIGNEDOUT) {
      return """
          Commands:
          register <username> <password> - to create an account
          login <username> <password> - to play chess
          quit - playing chess
          help - with possible commands
          """;
    } else {
      return """
          Commands:
          create <gameName> - a game
          list - games
          join <ID> [WHITE|BLACK] - a game
          observe <ID> - a game
          logout - when you are done
          quit - playing chess
          help - with possible commands
          """;
    }
  }
}
