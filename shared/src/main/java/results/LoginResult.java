package results;

// Used for the result of registering and logging in
public record LoginResult(String username, String authToken, String message) {
}
