package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
  private int gameId;
  private Session session;

  public Connection(int gameId, Session session) {
    this.gameId = gameId;
    this.session = session;
  }

  public void send(String msg) throws IOException {
    session.getRemote().sendString(msg);
  }

  public int getGameId() {
    return gameId;
  }

  public Session getSession() {
    return session;
  }
}
