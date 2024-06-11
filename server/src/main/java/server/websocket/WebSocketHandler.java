package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.WebsocketService;
import util.JsonSerializer;
import websocket.commands.UserGameCommand;
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
  public void onMessage(Session session, String message) {
    try {
      UserGameCommand command = JsonSerializer.deserialize(message, UserGameCommand.class);

//      String username = getUsername(command.getAuthString());
      String username = "placeholder";
      throw new ExceptionResult(400, "Not implemented");
//      switch (command.getCommandType()) {
//        case CONNECT -> connect(session, username, (ConnectCommand) command);
//        case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
//        case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
//        case RESIGN -> resign(session, username, (ResignCommand) command);
//      }
    } catch (ExceptionResult e) {
      // Potentially refactor this
      e.printStackTrace();
    }
  }
}
