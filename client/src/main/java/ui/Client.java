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
        case "creategame" -> createGame(params);
        case "listgames" -> listGames();
        case "playgame" -> playGame(params);
        case "observeGame" -> observeGame(params);
        case "quit" -> "quit";
        default -> help();
      };
    } catch (ExceptionResult ex) {
      return ex.getMessage();
    }
  }
}
