package model;

record UserData(String username, String password, String email) {
  public UserData updateUsername(String username) {
    return new UserData(username, this.password, this.email);
  }
  public UserData updatePassword(String password) {
    return new UserData(this.username, password, this.email);
  }
  public UserData updateEmail(String email) {
    return new UserData(this.username, this.password, email);
  }
}
