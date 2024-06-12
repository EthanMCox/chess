package ui.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ExceptionResult;
import sharedutil.JsonSerializer;
import websocket.messages.*;
import websocket.commands.*;

import javax.websocket.Endpoint;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketCommunicator extends Endpoint {
  Session session;
  NotificationHandler notificationHandler;

  public WebSocketCommunicator(String url, NotificationHandler notificationHandler) throws ExceptionResult {
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/ws");
      this.notificationHandler = notificationHandler;

      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);

      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {
          ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
          switch (notification.getServerMessageType()) {
            case NOTIFICATION -> notification = JsonSerializer.deserialize(message, NotificationMessage.class);
            case ERROR -> notification = JsonSerializer.deserialize(message, ErrorMessage.class);
            case LOAD_GAME -> notification = JsonSerializer.deserialize(message, LoadGameMessage.class);
          }
          notificationHandler.notify(notification);
        }
      });

    } catch (DeploymentException | IOException | URISyntaxException ex) {
      ex.printStackTrace();
      throw new ExceptionResult(500, ex.getMessage());
    }
  }

  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {
  }

  public void connectToGame(String authToken, Integer gameID) throws ExceptionResult {
    try {
      var command = new ConnectCommand(authToken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (IOException e) {
      throw new ExceptionResult(500, e.getMessage());
    }
  }

  public void makeMove(String authToken, Integer gameID, ChessMove move) throws ExceptionResult {
    try {
      MakeMoveCommand command = new MakeMoveCommand(authToken, gameID, move);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (IOException e) {
      throw new ExceptionResult(500, e.getMessage());
    }
  }

  public void leaveGame(String authToken, Integer gameID) throws ExceptionResult{
    try {
      LeaveCommand command = new LeaveCommand(authToken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
      this.session.close();
    } catch (IOException e) {
      throw new ExceptionResult(500, e.getMessage());
    }
  }

  public void resign(String authToken, Integer gameID) throws ExceptionResult {
    try {
      ResignCommand command = new ResignCommand(authToken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (IOException e) {
      throw new ExceptionResult(500, e.getMessage());
    }
  }
}
