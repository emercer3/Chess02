package websocket;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;

@WebSocket
public class WebSocketHandler {
  private final ConnectionManager connections = new ConnectionManager();

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
    UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
    
    switch(action.getCommandType()) {
      case CONNECT -> connect();
      case MAKE_MOVE -> makeMove();
      case LEAVE -> leave();
      case RESIGN -> resign();
    }
  }

  private void connect() throws IOException {

  }

  private void makeMove() throws IOException {

  }

  private void leave() throws IOException {

  }

  private void resign() throws IOException {

  }
}
