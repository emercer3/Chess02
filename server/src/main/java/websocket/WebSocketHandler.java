package websocket;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import dataaccess.sqldataaccess.*;
import model.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessage.ServerMessageType;

import java.io.IOException;
import java.util.Timer;

@WebSocket
public class WebSocketHandler {
  private final ConnectionManager connections = new ConnectionManager();
  private final dataaccess.UserDataAccess userData;
  private final dataaccess.AuthDataAccess authData;
  private final dataaccess.GameDataAccess gameData;

  public WebSocketHandler(dataaccess.UserDataAccess userData, dataaccess.AuthDataAccess authData, dataaccess.GameDataAccess gameData) {
    this.userData = userData;
    this.authData = authData;
    this.gameData = gameData;
  }

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
    UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
    
    switch(action.getCommandType()) {
      case CONNECT -> connect(action, session);
      case MAKE_MOVE -> makeMove(action, session);
      case LEAVE -> leave(action, session);
      case RESIGN -> resign();
    }
  }

  private void connect(UserGameCommand userGameinfo, Session session) throws IOException {
    AuthData auth = null;
    GameData game = null;

    try {
      auth = authData.getAuthData(userGameinfo.getAuthToken());
      game = gameData.getGame(userGameinfo.getGameID());
    } catch (Exception e) {

    }

    connections.add(auth.username(), session);
    var notification = new ServerMessage(ServerMessageType.NOTIFICATION);
    var message = String.format("%s joined the game ", auth.username());
    notification.setMsg(message);
    connections.broadcast(auth.username(), notification);

    var loadGame = new ServerMessage(ServerMessageType.LOAD_GAME);
    loadGame.setGame(game);
    connections.send(auth.username(), loadGame);          // how to not broadcast game to everyone.

  }

  private void makeMove(UserGameCommand userGameinfo, Session session) throws IOException {

  }

  private void leave(UserGameCommand userGameinfo, Session session) throws IOException {
    AuthData auth = null;

    try {
      auth = authData.getAuthData(userGameinfo.getAuthToken());
    } catch (Exception e) {

    }

    connections.remove(auth.username());
    var notification = new ServerMessage(ServerMessageType.NOTIFICATION);
    var message = String.format("%s left the game ", auth.username());
    notification.setMsg(message);
    connections.broadcast(auth.username(), notification);
  }

  private void resign() throws IOException {

  }
}
