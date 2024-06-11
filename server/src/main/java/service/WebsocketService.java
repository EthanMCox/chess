package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.*;

import java.util.HashSet;
import java.util.Map;


public class WebsocketService {
  private final AuthDAO authDAO;
  private final GameDAO gameDAO;
  private final UserDAO userDAO;

  public WebsocketService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    this.authDAO = authDAO;
    this.gameDAO = gameDAO;
    this.userDAO = userDAO;
  }

  public void connect(Session session, String username, ConnectCommand command, Map<Integer, HashSet<Session>> connections) {
    // Stub
  }

  public void makeMove(Session session, String username, MakeMoveCommand command, Map<Integer, HashSet<Session>> connections) {
    // Stub
  }

  public void leaveGame(Session session, String username, LeaveCommand command, Map<Integer, HashSet<Session>> connections) {
    // Stub
  }

  public void resign(Session session, String username, ResignCommand command, Map<Integer, HashSet<Session>> connections) {
    // Stub
  }
}
