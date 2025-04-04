package websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.*;
import websocket.messages.*;
import chess.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
  Session session;
  NotificationHandler notificationHandler;

  public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
    try {
      url = url.replace("http", "ws");
      URI socketUri = new URI(url + "/ws");
      this.notificationHandler = notificationHandler;

      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketUri);

      this.session.addMessageHandler(new MessageHandler.Whole<String>(){
        @Override
        public void onMessage(String message) {
          ServerMessage msg =  new Gson().fromJson(message, ServerMessage.class);
          notificationHandler.notify(msg);
        }
      });
    } catch (DeploymentException | IOException | URISyntaxException e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  @Override
  public void onOpen(Session arg0, EndpointConfig arg1) {
  }

  public void joinGame(String authToken, int gameId) throws ResponseException {
    try {
      var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId, null);
      this.session.getBasicRemote().sendText(new Gson().toJson(action));
    } catch (IOException e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  public void makeMove(String authToken, int gameId, ChessMove move) throws ResponseException {
    try {
      var action = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameId, move);
      this.session.getBasicRemote().sendText(new Gson().toJson(action));
    } catch (IOException e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  public void resign(String authToken, int gameId) throws ResponseException {
    try {
      var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameId, null);
      this.session.getBasicRemote().sendText(new Gson().toJson(action));
    } catch (IOException e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  public void leaveGame(String authToken, int gameId) throws ResponseException {
    try {
      var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameId, null);
      this.session.getBasicRemote().sendText(new Gson().toJson(action));
      this.session.close();
    } catch (IOException e) {
      throw new ResponseException(500, e.getMessage());
    }
  }
  
}
