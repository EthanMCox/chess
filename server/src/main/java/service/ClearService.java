package service;

import dataaccess.*;
import exception.ExceptionResult;
import results.SuccessResult;

public class ClearService {
  private final AuthDAO authDAO;
  private final GameDAO gameDAO;
  private final UserDAO userDAO;
  public ClearService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    this.authDAO = authDAO;
    this.gameDAO = gameDAO;
    this.userDAO = userDAO;
  }

  public SuccessResult clearAll() throws ExceptionResult {
    clearAllAuths();
    clearAllGames();
    clearAllUsers();
    return new SuccessResult();
  }
  private void clearAllAuths() throws ExceptionResult{
    authDAO.clear();
  }
  private void clearAllGames() throws ExceptionResult{
    gameDAO.clear();
  }
  private void clearAllUsers() throws ExceptionResult{
    userDAO.clear();
  }
}
