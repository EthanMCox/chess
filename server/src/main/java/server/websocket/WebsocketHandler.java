package server.websocket;

import dataaccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.WebsocketService;
import util.JsonSerializer;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebsocketHandler {
  private WebsocketService websocketService;
  private final ConnectionManager connections = new ConnectionManager();

  public WebsocketHandler(WebsocketService websocketService) {
    this.websocketService = websocketService;
  }

  @OnWebSocketMessage
  public void onMessage(Session session, String message) {
    try {
      UserGameCommand command = JsonSerializer.deserialize(message, UserGameCommand.class);

//      String username = getUsername(command.getAuthString());
      String username = "placeholder";


    }
  }
}
