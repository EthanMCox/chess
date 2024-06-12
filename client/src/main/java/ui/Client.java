package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ExceptionResult;
import model.*;
import results.*;
import serverclientcommunication.ServerFacade;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketCommunicator;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class Client {
  private final ServerFacade server;
  private String serverUrl;
  private State state = State.SIGNEDOUT;
  private String username = null;
  private String authToken = null;
  private HashMap<Integer, Integer> listedGames = null;
  private Integer joinedGame = null;
  private GameRole role = GameRole.NONE;
  private ChessGame game = null;
  private WebSocketCommunicator ws = null;

  public enum GameRole {
    WHITE,
    BLACK,
    OBSERVER,
    NONE
  }
  private final NotificationHandler notificationHandler;

  public Client(String serverUrl, NotificationHandler notificationHandler) {
    this.server = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
    this.notificationHandler = notificationHandler;
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
        case "redraw" -> redraw();
        case "move" -> makeMove(params);
        case "highlight" -> highlightMoves(params);
        case "leave" -> leaveGame();
        case "resign" -> resignGame();
        case "quit" -> quit();
        default -> help();
      };
    } catch (ExceptionResult ex) {
      return ex.getMessage();
    }
  }

  public String register(String... params) throws ExceptionResult {
    if (state == State.SIGNEDIN) {
      throw new ExceptionResult(400, "You are already logged in. Logout to switch accounts");
    }
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
    if (state == State.GAMEPLAY) {
      return "You must leave the game before logging out";
    }
    SuccessResult response = server.logout(authToken);
    if (response != null) {
      state = State.SIGNEDOUT;
      authToken = null;
      username = null;
      listedGames = null;
      joinedGame = null;
      role = GameRole.NONE;
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
        return String.format("%s created", gameName);
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
      if (listedGames == null) {
        listedGames = new HashMap<>();
      } else {
        listedGames.clear();
      }
      if (games.isEmpty()) {
        message.append("No games available\n");
        return message.toString();
      }
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
    if (state == State.GAMEPLAY) {
      throw new ExceptionResult(400, "You are already in a game. Leave to join another game");
    }
    if (listedGames == null) {
      throw new ExceptionResult(400, "List games to view available games");
    }
    if (params.length >= 2) {
      Integer gameID = listedGames.get(Integer.parseInt(params[0]));
      if (gameID == null) {
        throw new ExceptionResult(400, "Invalid game. List games to view available games");
      }
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
        role = teamColor == ChessGame.TeamColor.WHITE ? GameRole.WHITE : GameRole.BLACK;
        state = State.GAMEPLAY;
        joinedGame = gameID;
        ws = new WebSocketCommunicator(serverUrl, notificationHandler);
        ws.connectToGame(authToken, gameID);
        return "";
      }
      throw new ExceptionResult(400, "Error: unable to join game");
    }
    throw new ExceptionResult(400, "Expected: join <ID> [WHITE|BLACK]");
  }

  public String observeGame(String... params) throws ExceptionResult {
    if (state == State.SIGNEDOUT) {
      throw new ExceptionResult(400, "You must be logged in to observe a game");
    }
    if (state == State.GAMEPLAY || joinedGame != null) {
      throw new ExceptionResult(400, "You are already in a game. Leave to observe another game");
    }
    if (params.length >= 1) {
      Integer gameID = listedGames.get(Integer.parseInt(params[0]));
      if (gameID == null) {
        throw new ExceptionResult(400, "Invalid game ID. List games to view available games");
      }
      joinedGame = gameID;
      role = GameRole.OBSERVER;
      state = State.GAMEPLAY;
      ws = new WebSocketCommunicator(serverUrl, notificationHandler);
      ws.connectToGame(authToken, gameID);
      return "";
    }
    throw new ExceptionResult(400, "Expected: observe <ID>");
  }

  public String redraw() {
    if (state != State.GAMEPLAY || role == GameRole.NONE) {
      return "You must join a game to redraw the board";
    }
    if (game == null) {
      return "Error: no game available to redraw";
    }
    ChessGame.TeamColor teamColor = role == GameRole.BLACK ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
    ChessBoardWriter.drawChessBoard(System.out, teamColor, game.getBoard());
    return "";
  }

  public String makeMove(String... params) throws ExceptionResult {
    if (state != State.GAMEPLAY || role == GameRole.NONE) {
      return "You must join a game to make a move";
    }
    if (role == GameRole.OBSERVER) {
      return "Observers cannot make moves";
    }
    if (game == null) { // Do I need this?
      return "Error: no game available to make a move";
    }
    if (params.length >= 2) {
      ChessPosition from;
      ChessPosition to;
      try {
        from = new ChessPosition(params[0]);
        to = new ChessPosition(params[1]);
      } catch (ExceptionResult ex) {
        return "Error: " + ex.getMessage();
      }
      ChessMove move;
      if (params.length == 4) {
        if (!params[0].equals("->") || !validPromotionPiece(params[3])) {
          return "Expected: move <from> <to> -> <promotionPiece>\n    Ex: move e7 e8 -> q OR move f2 f4";
        }
        ChessPiece.PieceType promotionPiece;
        switch (params[3].toLowerCase()) {
          case "r" -> promotionPiece = ChessPiece.PieceType.ROOK;
          case "b" -> promotionPiece = ChessPiece.PieceType.BISHOP;
          case "n" -> promotionPiece = ChessPiece.PieceType.KNIGHT;
          default -> promotionPiece = ChessPiece.PieceType.QUEEN;
        }
        move = new ChessMove(from, to, promotionPiece);
      } else {
        move = new ChessMove(from, to, null);
      }
      ws.makeMove(authToken, joinedGame, move);
      return "";
    }
    return "Error: unable to make move";
  }

  private boolean validPromotionPiece(String promotionPiece) {
    return promotionPiece.equalsIgnoreCase("q") || promotionPiece.equalsIgnoreCase("r") ||
            promotionPiece.equalsIgnoreCase("b") || promotionPiece.equalsIgnoreCase("n");
  }

  public String highlightMoves(String... params) throws ExceptionResult {
    if (state != State.GAMEPLAY || role == GameRole.NONE) {
      return "You must join a game to highlight moves";
    }
    if (game == null) {
      return "Error: no game available to highlight moves";
    }
    if (params.length >= 1) {
      ChessPosition position;
      try {
        position = new ChessPosition(params[0]);
      } catch (ExceptionResult ex) {
        return "Error: " + ex.getMessage();
      }
      ChessGame.TeamColor teamColor = role == GameRole.BLACK ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
      ChessBoardWriter.drawChessBoard(System.out, teamColor, game, position);
    }
    return "";
  }

  public String leaveGame() {
    return "placeholder";
  }

  public String resignGame() {
    return "placeholder";
  }

  public String quit() {
    return "quit";
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
    } else if(state == State.SIGNEDIN){
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
    } else {
      return """
          Commands:
          redraw - the board
          move <from> <to> -> <promotionPiece> - a piece
              Ex: move e7 e8 -> q OR move f2 f4
          highlight <position> - legal moves
              Ex: highlight c5
          leave - the game
          resign - the game
          help - with possible commands
          """;
    }
  }

  public GameRole getRole() {
    return role;
  }

  public void setGame(ChessGame game) {
    this.game = game;
  }
}
