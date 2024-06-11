package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

import exception.ExceptionResult;
import model.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.*;
import websocket.messages.*;

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

  public void connect(Session session, ConnectCommand command, Map<Integer, HashSet<Session>> connections) throws ExceptionResult {
    connections.get(command.getGameID()).add(session);
    AuthData auth = authDAO.getAuth(command.getAuthString());
    if (auth == null) {
      throw new ExceptionResult(401, "Error: unauthorized");
    }
    String role;
    String username = auth.username();
    GameData gameData = gameDAO.getGame(command.getGameID());
    if (username.equals(gameData.whiteUsername())) {
      role = "white";
    } else if (username.equals(gameData.blackUsername())){
      role = "black";
    }
    else {
      role = "an observer";
    }
    var message = String.format("%s has joined the game as %s", username, role);
    ServerMessage Notification = new Notification(message);
  }

  public void makeMove(Session session, MakeMoveCommand command, Map<Integer, HashSet<Session>> connections) {
    // Stub
  }

  public void leaveGame(Session session, LeaveCommand command, Map<Integer, HashSet<Session>> connections) {
    // Stub
  }

  public void resign(Session session, ResignCommand command, Map<Integer, HashSet<Session>> connections) {
    // Stub
  }
}
