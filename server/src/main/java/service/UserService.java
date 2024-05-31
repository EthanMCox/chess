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

import static util.Security.*;

public class UserService {
  private final UserDAO userDAO;
  private final AuthDAO authDAO;

  public UserService(UserDAO userDAO, AuthDAO authDAO) {
    this.userDAO = userDAO;
    this.authDAO = authDAO;
  }
  public LoginResult register(RegisterRequest request) throws ExceptionResult {
    if (request.username() == null || request.password() == null || request.email() == null) {
      throw new ExceptionResult(400, "Error: bad request");
    }
    UserData user = userDAO.getUser(request.username());
    if (user != null) {
      throw new ExceptionResult(403, "Error: already taken");
    }
    String hashedPassword = encyptPassword(request.password());
    userDAO.createUser(request.username(), hashedPassword, request.email());
    AuthData auth = authDAO.createAuth(request.username());
    return new LoginResult(auth.username(), auth.authToken());
  }

  public LoginResult login(LoginRequest request) throws ExceptionResult {
    UserData user = userDAO.getUser(request.username());
    if (user == null) {
      throw new ExceptionResult(401, "Error: unauthorized");
    }
    if (!passwordMatches(user, request.password())) {
      throw new ExceptionResult(401, "Error: unauthorized");
    }
    AuthData auth = authDAO.createAuth(request.username());
    return new LoginResult(auth.username(), auth.authToken());
  }
  public SuccessResult logout(AuthRequest request) throws ExceptionResult{
    AuthData auth = authDAO.getAuth(request.authToken());
    if (auth == null) {
      throw new ExceptionResult(401, "Error: unauthorized");
    }
    authDAO.deleteAuth(auth);
    return new SuccessResult();
  }
}

