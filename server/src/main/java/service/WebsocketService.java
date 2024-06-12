package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

import exception.ExceptionResult;
import model.*;
import org.eclipse.jetty.websocket.api.Session;
import util.JsonSerializer;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;
import java.util.ArrayList;
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
    checkIfAuthorized(auth);

    String role;
    String username = auth.username();
    GameData gameData = gameDAO.getGame(command.getGameID());
    if (gameData == null) {
      broadcastToSelf(command.getGameID(), new ErrorMessage("Error: game not found"), connections, session);
      removeConnection(command.getGameID(), session, connections);
      return;
    }
    if (username.equals(gameData.whiteUsername())) {
      role = "white";
    } else if (username.equals(gameData.blackUsername())){
      role = "black";
    }
    else {
      role = "an observer";
    }
    ChessGame game = gameData.game();
    broadcastToSelf(command.getGameID(), new LoadGameMessage(game), connections, session);
    var message = String.format("%s has joined the game as %s", username, role);
    ServerMessage notification = new NotificationMessage(message);
    broadcast(command.getGameID(), notification, connections, session);
  }

  public void makeMove(Session session, MakeMoveCommand command, Map<Integer, HashSet<Session>> connections) throws ExceptionResult {
    AuthData auth = authDAO.getAuth(command.getAuthString());
    checkIfAuthorized(auth);

    ChessMove move = command.getMove();
    if (move == null) {
      broadcastToSelf(command.getGameID(), new ErrorMessage("Error: invalid move"), connections, session);
      return;
    }

    String username = auth.username();
    GameData gameData = gameDAO.getGame(command.getGameID());

    if (gameData == null) {
      broadcastToSelf(command.getGameID(), new ErrorMessage("Error: game not found. Please leave and rejoin another game"), connections, session);
      removeConnection(command.getGameID(), session, connections);
      return;
    }
    if (notAPlayer(username, gameData)) {
      broadcastToSelf(command.getGameID(), new ErrorMessage("Error: you are not a player in this game"), connections, session);
      return;
    }
    ChessGame game = gameData.game();
    if (game.getTeamWon() == ChessGame.TeamWon.DRAW) { // Game already stalemate
      broadcastToSelf(command.getGameID(), new ErrorMessage("Error: Game has already concluded in stalemate"), connections, session);
      return;
    }
    if (game.getTeamWon() != ChessGame.TeamWon.NONE) { // Someone already won
      String winningTeam = game.getTeamWon() == ChessGame.TeamWon.WHITE ? "White" : "Black";
      String message = String.format("Error: Game has already concluded. %s has won", winningTeam);
      broadcastToSelf(command.getGameID(), new ErrorMessage(message), connections, session);
      return;
    }
    if (isWrongTurn(game, gameData, username)) {
      broadcastToSelf(command.getGameID(), new ErrorMessage("Error: it is not your turn"), connections, session);
      return;
    }
    try {
      game.makeMove(command.getMove());
    } catch (InvalidMoveException e) {
      broadcastToSelf(command.getGameID(), new ErrorMessage("Error: " + e.getMessage()), connections, session);
      return;
    }

    gameData = gameData.setGame(game);
    gameDAO.updateGame(gameData);

    broadcast(command.getGameID(), new LoadGameMessage(game), connections);
    sendMoveNotification(session, command, connections, move, username);
    sendGameStatusNotification(command, connections, game);
  }

  private void sendGameStatusNotification(MakeMoveCommand command, Map<Integer, HashSet<Session>> connections, ChessGame game) throws ExceptionResult {
    ChessGame.TeamColor teamTurn = game.getTeamTurn();
    String turn = teamTurn == ChessGame.TeamColor.WHITE ? "White" : "Black";
    String message = null;
    if (game.isInCheckmate(teamTurn)) {
      message = String.format("%s is in checkmate. %s wins!", turn, turn.equals("White") ? "Black" : "White");
    }
    else if (game.isInCheck(teamTurn)) {
      message = String.format("%s is in check", turn);
    }
    else if (game.isInStalemate(teamTurn)) {
      message = "The game has ended in stalemate";
    }
    if (message != null) {
      broadcast(command.getGameID(), new NotificationMessage(message), connections);
    }
  }

  private void sendMoveNotification(Session session, MakeMoveCommand command, Map<Integer, HashSet<Session>> connections, ChessMove move, String username) throws ExceptionResult {
    String startPosition = move.getStartPosition().getPositionAsString();
    String endPosition = move.getEndPosition().getPositionAsString();
    String promotionPiece = move.getPromotionPieceAsString();
    String message;
    if (promotionPiece != null) {
      message = String.format("%s moved %s to %s and promoted to %s", username, startPosition, endPosition, promotionPiece);
    } else {
      message = String.format("%s moved %s to %s", username, startPosition, endPosition);
    }
    ServerMessage notification = new NotificationMessage(message);
    broadcast(command.getGameID(), notification, connections, session);
  }

  private static boolean notAPlayer(String username, GameData gameData) {
    return !username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername());
  }

  private static boolean isWrongTurn(ChessGame game, GameData gameData, String username) {
    return (game.getTeamTurn() == ChessGame.TeamColor.WHITE && !username.equals(gameData.whiteUsername())) || (game.getTeamTurn() == ChessGame.TeamColor.BLACK && !username.equals(gameData.blackUsername()));
  }

  public void leaveGame(Session session, LeaveCommand command, Map<Integer, HashSet<Session>> connections) {
    // Stub
  }

  public void resign(Session session, ResignCommand command, Map<Integer, HashSet<Session>> connections) {
    // Stub
  }

  private void checkIfAuthorized(AuthData auth) throws ExceptionResult {
    if (auth == null) {
      throw new ExceptionResult(401, "Error: unauthorized");
    }
  }

  private void broadcast(Integer gameID, ServerMessage notification, Map<Integer, HashSet<Session>> connections) throws ExceptionResult {
    broadcast(gameID, notification, connections, null);
  }

  private void broadcast(Integer gameID, ServerMessage notification, Map<Integer, HashSet<Session>> connections, Session excludedSession) throws ExceptionResult {
    var removeList = new ArrayList<Session>();
    for (var session : connections.get(gameID)) {
      if (session.isOpen()) {
        if (!session.equals(excludedSession)) {
          sendMessage(session, notification);
        }
      } else {
        removeList.add(session);
      }
    }

    // Clean up sessions that have been closed in the game
    for (var session : removeList) {
      connections.get(gameID).remove(session);
    }
  }

  private void broadcastToSelf(Integer gameID, ServerMessage notification, Map<Integer, HashSet<Session>> connections, Session session) throws ExceptionResult {
    if (connections.get(gameID).contains(session)) {
      if (session.isOpen()) {
        sendMessage(session, notification);
      } else {
        connections.get(gameID).remove(session);
      }
    }
  }

  public void sendMessage(Session session, ServerMessage message) throws ExceptionResult {
    try {
      session.getRemote().sendString(JsonSerializer.serialize(message));
    } catch (IOException e) {
      throw new ExceptionResult(500, e.getMessage());
    }
  }

  private void removeConnection(Integer gameID, Session session, Map<Integer, HashSet<Session>> connections) {
    connections.get(gameID).remove(session);
  }
}
