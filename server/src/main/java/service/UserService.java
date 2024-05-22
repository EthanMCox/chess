package service;

import com.sun.net.httpserver.Authenticator;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import requests.LoginRequest;
import requests.LogoutRequest;
import requests.RegisterRequest;
import results.*;

public class UserService {
  private final UserDAO userDAO;
  private AuthDAO authDAO;

  public UserService(UserDAO userDAO, AuthDAO authDAO) {
    this.userDAO = userDAO;
    this.authDAO = authDAO;
  }
  public LoginResult register(RegisterRequest request) {
    return null;
  }
  public LoginResult login(LoginRequest request) {
    return null;
  }
  public SuccessResult logout(LogoutRequest request) {

    return new SuccessResult();
  }

  public SuccessResult clearAllUsers() {
    userDAO.clear();
    return new SuccessResult();
  }
}

