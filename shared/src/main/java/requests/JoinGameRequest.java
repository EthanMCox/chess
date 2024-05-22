package requests;

public record JoinGameRequest(String authToken, String playerColor, int gameId) {
  public JoinGameRequest setAuthToken(String authToken) {
    return new JoinGameRequest(authToken, playerColor, gameId);
  }
}
