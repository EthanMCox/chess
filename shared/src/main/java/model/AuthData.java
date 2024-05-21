package model;
public record AuthData(String authToken, String username) {
  public AuthData updateAuthToken(String authToken) {
    return new AuthData(authToken, this.username);
  }
  public AuthData updateUsername(String username) {
    return new AuthData(this.authToken, username);
  }
}
