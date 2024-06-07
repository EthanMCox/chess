package server;

import chess.ChessGame;
import exception.ExceptionResult;
import requests.*;
import results.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public LoginResult register(String username, String password, String email) throws ExceptionResult {
      var path = serverUrl + "/user";
      RegisterRequest request = new RegisterRequest(username, password, email);
      return ClientCommunicator.makeRequest("POST", path, request, LoginResult.class, null);
    }

    public LoginResult login(String username, String password) throws ExceptionResult {
      var path = serverUrl + "/session";
      LoginRequest request = new LoginRequest(username, password);
      return ClientCommunicator.makeRequest("POST", path, request, LoginResult.class, null);
    }

    public SuccessResult logout(String authToken) throws ExceptionResult {
      var path = serverUrl + "/session";
      AuthRequest request = new AuthRequest(authToken);
      return ClientCommunicator.makeRequest("DELETE", path, request, SuccessResult.class, authToken);
    }

    public CreateGameResult createGame(String gameName, String authToken) throws ExceptionResult {
      var path = serverUrl + "/game";
      CreateGameRequest request = new CreateGameRequest(gameName, authToken);
      return ClientCommunicator.makeRequest("POST", path, request, CreateGameResult.class, authToken);
    }

    public ListGamesResult listGames(String authToken) throws ExceptionResult {
      var path = serverUrl + "/game";
      AuthRequest request = new AuthRequest(authToken);
      return ClientCommunicator.makeRequest("GET", path, request, ListGamesResult.class, authToken);
    }

    public SuccessResult joinGame(int gameID, ChessGame.TeamColor teamColor, String authToken) throws ExceptionResult {
      var path = serverUrl + "/game";
      JoinGameRequest request = new JoinGameRequest(null, teamColor, gameID);
      return ClientCommunicator.makeRequest("PUT", path, request, SuccessResult.class, authToken);
    }
}
