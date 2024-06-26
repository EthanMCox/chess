package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import exception.ExceptionResult;
import model.AuthData;
import model.GameData;
import requests.AuthRequest;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import results.*;
import dataaccess.GameDAO;

public class GameService {
  private final GameDAO gameDAO;
  private final AuthDAO authDAO;

  public GameService(GameDAO gameDAO, AuthDAO authDAO) {
    this.gameDAO = gameDAO;
    this.authDAO = authDAO;
  }

  public ListGamesResult listGames(AuthRequest request) throws ExceptionResult {
    if (request.authToken() == null) {
      throw new ExceptionResult(400, "Error: bad request");
    }
    AuthData auth = authDAO.getAuth(request.authToken());
    if (auth == null) {
      throw new ExceptionResult(401, "Error: unauthorized");
    }
    return new ListGamesResult(gameDAO.listGames());
  }

  public CreateGameResult createGame(CreateGameRequest request) throws ExceptionResult{
    if (request.gameName() == null || request.authToken() == null) {
      throw new ExceptionResult(400, "Error: bad request");
    }
    AuthData auth = authDAO.getAuth(request.authToken());
    if (auth == null) {
      throw new ExceptionResult(401, "Error: unauthorized");
    }
    return new CreateGameResult(gameDAO.createGame(request.gameName()));
  }

  public SuccessResult joinGame(JoinGameRequest request) throws ExceptionResult{
    if (request.playerColor() == null || request.gameID() == null  || request.authToken() == null) {
      throw new ExceptionResult(400, "Error: bad request");
    }
    AuthData auth = authDAO.getAuth(request.authToken());
    if (auth == null) {
      throw new ExceptionResult(401, "Error: unauthorized");
    }
    GameData game = gameDAO.getGame(request.gameID());
    if (game == null) {
      throw new ExceptionResult(400, "Error: bad request");
    }
    String currentPlayer = getCurrentPlayer(game, request.playerColor());
    if (currentPlayer != null) {
      throw new ExceptionResult(403, "Error: already taken");
    }
    String username = auth.username();
    if (request.playerColor() == ChessGame.TeamColor.WHITE) {
      game = game.setWhiteUsername(username);
    } else if (request.playerColor() == ChessGame.TeamColor.BLACK) {
      game = game.setBlackUsername(username);
    }
    gameDAO.updateGame(game);

    return new SuccessResult();
  }

  private String getCurrentPlayer(GameData game, ChessGame.TeamColor playerColor) throws ExceptionResult {
    if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
      return game.whiteUsername();
    } else if (playerColor.equals(ChessGame.TeamColor.BLACK)) {
      return game.blackUsername();
    } else {
      throw new ExceptionResult(400, "Error: bad request");
    }
  }
}
