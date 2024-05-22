package service;

import dataaccess.AuthDAO;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.ListGamesRequest;
import results.*;
import dataaccess.GameDAO;

public class GameService {
  private final GameDAO gameDAO;
  private final AuthDAO authDAO;

  public GameService(GameDAO gameDAO, AuthDAO authDAO) {
    this.gameDAO = gameDAO;
    this.authDAO = authDAO;
  }

  public ListGamesResult listGames(ListGamesRequest request) {

    return null;
  }

  public CreateGameResult createGame(CreateGameRequest request) {

    return null;
  }

  public SuccessResult joinGame(JoinGameRequest request) {

    return null;
  }
}
