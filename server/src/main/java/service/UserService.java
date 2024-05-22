package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exception.ExceptionResult;
import model.AuthData;
import model.UserData;
import requests.LoginRequest;
import requests.AuthRequest;
import requests.RegisterRequest;
import results.*;

public class UserService {
  private final UserDAO userDAO;
  private final AuthDAO authDAO;

  public UserService(UserDAO userDAO, AuthDAO authDAO) {
    this.userDAO = userDAO;
    this.authDAO = authDAO;
  }
  public LoginResult register(RegisterRequest request) throws ExceptionResult {
    UserData user = userDAO.getUser(request.username());
    if (user != null) {
      throw new ExceptionResult(403, "Error: already taken");
    }
    userDAO.createUser(request.username(), request.password(), request.email());
    AuthData auth = authDAO.createAuth(request.username());
    return new LoginResult(auth.username(), auth.authToken());
  }
  public LoginResult login(LoginRequest request) throws ExceptionResult {
    return null;
  }
  public SuccessResult logout(AuthRequest request) throws ExceptionResult{

    return new SuccessResult();
  }
}

