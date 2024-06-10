package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final ConcurrentHashMap<Integer, HashSet<Session>> connections = new ConcurrentHashMap<>();

  public void add(int gameId, Session session) {
    var connection = connections.computeIfAbsent(gameId, key -> new HashSet<>());
    connection.add(session);
  }

  public void remove(int gameId) {
    // May need to rewrite this
    connections.remove(gameId);
  }

  public void broadcast(int excludeGameId, ServerMessage notification) throws IOException {
//    var removeList = new ArrayList<Session>();
//    for (var c : connections.values()) {
//      if (c.isOpen()) {
//        if (c.getGameId() != excludeGameId) {
//          c.send(notification.toString());
//        }
//      } else {
//        removeList.add(c);
//      }
//    }

    // Clean up any connections that were left open.
//    for (var c : removeList) {
//      connections.remove(c.getGameId());
//    }
  }
}
