package requests;

public record CreateGameRequest(String gameName, String authToken) {
  public CreateGameRequest setAuthToken(String authToken) {
    return new CreateGameRequest(gameName, authToken);
  }
}
