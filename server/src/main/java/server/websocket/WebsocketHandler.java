package server.websocket;

import dataaccess.*;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class WebsocketHandler {
  private UserDAO userDAO;
  public WebsocketHandler() {

  }
}
