package requests;

import chess.ChessGame;

public record JoinGameRequest(String authToken, ChessGame.TeamColor playerColor, int gameId) {
  public JoinGameRequest setAuthToken(String authToken) {
    return new JoinGameRequest(authToken, playerColor, gameId);
  }
}
