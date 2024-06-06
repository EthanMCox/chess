package ui;

import exception.ExceptionResult;
import results.*;
import server.ServerFacade;

import java.util.Arrays;

public class Client {
  private final ServerFacade server;
  private State state = State.SIGNEDOUT;
  private String username = null;
  private String authToken = null;

  public Client(String serverUrl) {
    this.server = new ServerFacade(serverUrl);
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
    if (params.length >= 3) {
      var username = params[0];
      var password = params[1];
      var email = params[2];
      LoginResult response = server.register(username, password, email);

      if (response != null) {
        authToken = response.authToken();
        this.username = response.username();
        state = State.SIGNEDIN;
        return String.format("You are logged in as %s.", this.username);
      } else {
        throw new ExceptionResult(400, "Error: unable to login");
      }

    }

    throw new ExceptionResult(400, "Expected: register <username> <password> <email>");
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
