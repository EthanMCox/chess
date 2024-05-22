package service;

import dataaccess.AuthDAO;
import results.SuccessResult;

public class AuthService {
  private final AuthDAO authDAO;
  public AuthService(AuthDAO authDAO) {
    this.authDAO = authDAO;
  }

  public SuccessResult clearAllAuths() {
    authDAO.clear();
    return new SuccessResult();
  }
}
