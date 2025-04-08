package websocket;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;

import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  private final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> games = new ConcurrentHashMap<>();

  public void add(String user, int gameId, Session session) {
    if (games.get(gameId) == null) {
      games.put(gameId, new ConcurrentHashMap());
    }
    var connections = games.get(gameId);
    var connection = new Connection(user, session);
    connections.put(user, connection);
    
  }

  public void remove(String user, int gameId) {
    games.get(gameId).remove(user);
  }

  public void broadcast(String excluded, int gameId, ServerMessage message) throws IOException {
    var removeList = new ArrayList<Connection>();

    for (var c : games.get(gameId).values()) {
      if (c.session.isOpen()) {
        if (!c.userName.equals(excluded)) {
          c.send(new Gson().toJson(message));
        }
      } else {
        removeList.add(c);
      }
    }

    for (var c : removeList) {
      games.get(gameId).remove(c.userName);
    }
  }

  public void send(String user, int gameId, ServerMessage message) throws IOException {
    var c = games.get(gameId).get(user);
    c.send(new Gson().toJson(message));
  }
}
