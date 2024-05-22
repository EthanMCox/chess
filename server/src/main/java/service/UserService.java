package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import requests.LoginRequest;
import requests.LogoutRequest;
import requests.RegisterRequest;

public class UserService {
  private UserDAO userDAO;
  private AuthDAO authDAO;

  public UserService(UserDAO userDAO, AuthDAO authDAO) {
    this.userDAO = userDAO;
    this.authDAO = authDAO;
  }
  public AuthData register(RegisterRequest request) {
    return null;
  }
  public AuthData login(LoginRequest request) {
    return null;
  }
  public void logout(LogoutRequest request) {}
}
public void deleteAllUsers() {
  userDAO.clear();
}
