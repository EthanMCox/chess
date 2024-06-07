package ui;

import chess.ChessGame;
import exception.ExceptionResult;
import model.*;
import results.*;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class Client {
  private final ServerFacade server;
  private State state = State.SIGNEDOUT;
  private String username = null;
  private String authToken = null;
  private HashMap<Integer, Integer> listedGames = new HashMap<>();

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
        case "login" -> login(params);
        case "logout" -> logout();
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
        return String.format("You are logged in as %s. Type help to view more options", this.username);
      } else {
        throw new ExceptionResult(400, "Error: unable to register");
      }

    }

    throw new ExceptionResult(400, "Expected: register <username> <password> <email>");
  }

  public String login(String... params) throws ExceptionResult {
    if (state == State.SIGNEDIN) {
      throw new ExceptionResult(400, "You are already logged in. Logout to switch accounts");
    }
    if (params.length >= 2) {
      var username = params[0];
      var password = params[1];
      LoginResult response = server.login(username, password);

      if (response != null) {
        authToken = response.authToken();
        this.username = response.username();
        state = State.SIGNEDIN;
        return String.format("You are logged in as %s. Type help to view more options", this.username);
      } else {
        throw new ExceptionResult(400, "Error: unable to login");
      }
    }
    throw new ExceptionResult(400, "Expected: login <username> <password>");
  }

  public String logout() throws ExceptionResult {
    if (state == State.SIGNEDOUT) {
      return "You are already logged out";
    }
    SuccessResult response = server.logout(authToken);
    if (response != null) {
      state = State.SIGNEDOUT;
      authToken = null;
      username = null;
      return "You are logged out";
    }
    return "Error: unable to logout";
  }

  public String createGame(String... params) throws ExceptionResult {
    if (state == State.SIGNEDOUT) {
      throw new ExceptionResult(400, "You must be logged in to create a game");
    }
    if (params.length >= 1) {
      String gameName = params[0];
      CreateGameResult response = server.createGame(gameName, authToken);
      if (response != null) {
        return String.format("%s created with ID %s", gameName, response.gameID());
      }
      throw new ExceptionResult(400, "Error: unable to create game");
    }
    throw new ExceptionResult(400, "Expected: create <gameName>");
  }

  public String listGames() throws ExceptionResult {
    if (state == State.SIGNEDOUT) {
      throw new ExceptionResult(400, "You must be logged in to view games");
    }
    ListGamesResult response = server.listGames(authToken);
    if (response != null) {
      Collection<ListGamesData> games = response.games();
      StringBuilder message = new StringBuilder("Games:\n");
      int count = 1;
      listedGames.clear();
      for (ListGamesData game : games) {
        String whiteName = game.whiteUsername() == null ? "None" : game.whiteUsername();
        String blackName = game.blackUsername() == null ? "None" : game.blackUsername();
        message.append(String.format("%d. %s, White Player: %s, Black Player: %s\n",
                count, game.gameName(), whiteName, blackName));
        listedGames.put(count, game.gameID());
        count++;
      }
      return message.toString();
    }
    throw new ExceptionResult(400, "Error: unable to list games");
  }

  public String joinGame(String... params) throws ExceptionResult {
    if (state == State.SIGNEDOUT) {
      throw new ExceptionResult(400, "You must be logged in to join a game");
    }
    if (params.length >= 2) {
      int gameID = Integer.parseInt(params[0]);
      ChessGame.TeamColor teamColor;
      if (params[1].equalsIgnoreCase("white")) {
        teamColor = ChessGame.TeamColor.WHITE;
      } else if (params[1].equalsIgnoreCase("black")) {
        teamColor = ChessGame.TeamColor.BLACK;
      } else {
        throw new ExceptionResult(400, "Expected: join <ID> [WHITE|BLACK]");
      }
      SuccessResult response = server.joinGame(gameID, teamColor, authToken);
      if (response != null) {
        return String.format("%s joined game %d as %s", username, gameID, teamColor == ChessGame.TeamColor.WHITE ? "White" : "Black");
      }
      throw new ExceptionResult(400, "Error: unable to join game");
    }
    throw new ExceptionResult(400, "Expected: join <ID> [WHITE|BLACK]");
  }

  public String observeGame(String... params) throws ExceptionResult {
    return "placeholder";
  }

  public String help() {
    if (state == State.SIGNEDOUT) {
      return """
          Commands:
          register <username> <password> <email> - to create an account
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
