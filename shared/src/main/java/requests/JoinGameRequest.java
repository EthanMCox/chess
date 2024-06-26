package requests;

import chess.ChessGame;

public record JoinGameRequest(String authToken, ChessGame.TeamColor playerColor, Integer gameID) {
  public JoinGameRequest setAuthToken(String authToken) {
    return new JoinGameRequest(authToken, playerColor, gameID);
  }
}
