package requests;

public record AuthRequest(String authToken) {
  public AuthRequest setAuthToken(String authToken) {
    return new AuthRequest(authToken);
  }
}
