package websocket;

import websocket.messages.ServerMessage;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  private final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

  public void add(String user, Session session) {
    var connection = new Connection(user, session);
    connections.put(user, connection);
  }

  public void remove(String user) {
    connections.remove(user);
  }

  public void broadcast(String excluded, ServerMessage message) throws IOException {
    var removeList = new ArrayList<Connection>();

    for (var c : connections.values()) {
      if (c.session.isOpen()) {
        if (!c.userName.equals(excluded)) {
          c.send(message.toString());
        }
      } else {
        removeList.add(c);
      }
    }

    for (var c : removeList) {
      connections.remove(c.userName);
    }
  }
}
