package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.WebsocketService;
import util.JsonSerializer;
import websocket.commands.*;
import exception.ExceptionResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@WebSocket
public class WebSocketHandler {
  private WebsocketService websocketService;
  private final Map<Integer, HashSet<Session>> connections = new HashMap<>();

  public WebSocketHandler(WebsocketService websocketService) {
    this.websocketService = websocketService;
  }

  @OnWebSocketMessage
  public void onMessage(Session session, String message) { // Message is a JSON string representing a command object
    try {
      UserGameCommand command = JsonSerializer.deserialize(message, UserGameCommand.class);

      switch (command.getCommandType()) {
        case CONNECT -> websocketService.connect(session, (ConnectCommand) command, connections);
        case MAKE_MOVE -> websocketService.makeMove(session, (MakeMoveCommand) command, connections);
        case LEAVE -> websocketService.leaveGame(session, (LeaveCommand) command, connections);
        case RESIGN -> websocketService.resign(session, (ResignCommand) command, connections);
      }
    } catch (ExceptionResult e) {
      // Send message on error here
      e.printStackTrace();
    }
  }
}
