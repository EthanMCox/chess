package service;

import dataaccess.AuthDAO;
import exception.ExceptionResult;
import model.AuthData;
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
    AuthData auth = authDAO.getAuth(request.authToken());
    if (auth == null) {
      throw new ExceptionResult(401, "Error: unauthorized");
    }
    return new ListGamesResult(gameDAO.listGames());
  }

  public CreateGameResult createGame(CreateGameRequest request) throws ExceptionResult{
    AuthData auth = authDAO.getAuth(request.authToken());
    if (auth == null) {
      throw new ExceptionResult(401, "Error: unauthorized");
    }
    return new CreateGameResult(gameDAO.createGame(request.gameName()));
  }

  public SuccessResult joinGame(JoinGameRequest request) throws ExceptionResult{

    return null;
  }
}
