package ui.websocket;

import com.google.gson.Gson;
import exception.ExceptionResult;
import websocket.messages.*;
import websocket.commands.*;

import javax.websocket.Endpoint;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketCommunicator extends Endpoint {
  Session session;
  NotificationHandler notificationHandler;

  public WebsocketCommunicator(String url, NotificationHandler notificationHandler) throws ExceptionResult {
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
          notificationHandler.notify(notification);
        }
      });

    } catch (DeploymentException | IOException | URISyntaxException ex) {
      throw new ExceptionResult(500, ex.getMessage());
    }
  }

  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {
  }

  public void connectToGame(String authToken, Integer gameID) throws ExceptionResult {
    try {
      ConnectCommand command = new ConnectCommand(authToken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (IOException e) {
      throw new ExceptionResult(500, e.getMessage());
    }
  }

  public void makeMove() {

  }

  public void leaveGame() {

  }

  public void resign() {

  }

}
