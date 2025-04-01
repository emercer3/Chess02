package websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.*;
import websocket.messages.*;

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

  public void joinGame() throws ResponseException {

  }

  public void leaveGame() throws ResponseException {
    
  }
  
}
