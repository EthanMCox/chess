package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exception.ExceptionResult;
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
  public LoginResult register(RegisterRequest request) throws ExceptionResult {
    return null;
  }
  public LoginResult login(LoginRequest request) throws ExceptionResult {
    return null;
  }
  public SuccessResult logout(LogoutRequest request) throws ExceptionResult{

    return new SuccessResult();
  }

  public SuccessResult clearAllUsers() {
    userDAO.clear();
    return new SuccessResult();
  }
}

