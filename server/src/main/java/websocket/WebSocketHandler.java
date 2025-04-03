package websocket;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import dataaccess.sqldataaccess.*;
import model.*;
import chess.*;
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
    } catch (Exception e) {}

    if (auth == null) {
      var notification = new ServerMessage(ServerMessageType.ERROR);
      notification.setErrorMsg("Error: unauthorized");
      session.getRemote().sendString(new Gson().toJson(notification));
      return;
    } 
    
    connections.add(auth.username(), session);

    if (game == null) {
      var notification = new ServerMessage(ServerMessageType.ERROR);
      notification.setErrorMsg("Error: inccorect gameID");
      connections.send(auth.username(), notification);
    } else {
      var notification = new ServerMessage(ServerMessageType.NOTIFICATION);
      var message = String.format("%s joined the game", auth.username());
      notification.setMsg(message);
      connections.broadcast(auth.username(), notification);

      var loadGame = new ServerMessage(ServerMessageType.LOAD_GAME);
      loadGame.setGame(game);
      connections.send(auth.username(), loadGame);
    }
  }

  private void makeMove(UserGameCommand userGameinfo, Session session) throws IOException {
    AuthData auth = null;
    GameData chessGameData = null;

    try {
      auth = authData.getAuthData(userGameinfo.getAuthToken());
      chessGameData = gameData.getGame(userGameinfo.getGameID());
    } catch (Exception e) {}

    if (auth == null) {
      var notification = new ServerMessage(ServerMessageType.ERROR);
      notification.setErrorMsg("Error: unauthorized");
      session.getRemote().sendString(new Gson().toJson(notification));
      return;
    } else if (chessGameData == null) {
      var notification = new ServerMessage(ServerMessageType.ERROR);
      notification.setErrorMsg("Error: inccorect gameID");
      connections.send(auth.username(), notification);
    } else if (observerCheck(auth, chessGameData)) {
      var notification = new ServerMessage(ServerMessageType.ERROR);
      notification.setErrorMsg("Error: observers can't play");
      connections.send(auth.username(), notification);
    }
    
    else {
      ChessMove move = null;
      ChessGame chessGame = null;

      try {
        move = userGameinfo.getMove();
        chessGame = chessGameData.game();

        if (checkUsingOtherColorPiece(auth, chessGameData, chessGame, move)) {
          var notification = new ServerMessage(ServerMessageType.ERROR);
          notification.setErrorMsg("Error: can't move other color's pieces");
          connections.send(auth.username(), notification);
          return;
        } else if (chessGame.isInCheckmate(chessGame.getTeamTurn())) {
          var notification = new ServerMessage(ServerMessageType.ERROR);
          notification.setErrorMsg("Error: Game is over");
          connections.send(auth.username(), notification);
          return;
        }
        
        chessGame.makeMove(move);
        gameData.updateGame(chessGameData);

      } catch (Exception e) {
        var notification = new ServerMessage(ServerMessageType.ERROR);
        notification.setErrorMsg("Error: invalid move");
        connections.send(auth.username(), notification);
        return;
      }

      var loadGame = new ServerMessage(ServerMessageType.LOAD_GAME);
      loadGame.setGame(chessGameData);
      connections.broadcast(null, loadGame);

      var notification = new ServerMessage(ServerMessageType.NOTIFICATION);
      var message = String.format("%s made move" + move, auth.username());
      notification.setMsg(message);
      connections.broadcast(auth.username(), notification);

      if (chessGame.isInCheckmate(chessGame.getTeamTurn()) || chessGame.isInStalemate(chessGame.getTeamTurn()) || chessGame.isInCheck(chessGame.getTeamTurn())) {
        var notify = new ServerMessage(ServerMessageType.NOTIFICATION);
        notify.setMsg("is in check/checkmate/stalemate");
      }
    }
  }

  private boolean checkUsingOtherColorPiece(AuthData auth, GameData game, ChessGame chessGame, ChessMove move) {
    if (game.blackUsername() == auth.username() && chessGame.getBoard().getPiece(move.getStartPosition()).getTeamColor().equals("BLACK")) {
      return false;
    } else if (game.whiteUsername() == auth.username() && chessGame.getBoard().getPiece(move.getStartPosition()).getTeamColor().equals("WHITE")) {
      return false;
    } else {
      return true;
    }
  }

  private boolean observerCheck(AuthData auth, GameData game) {
    if (auth.username().equals(game.whiteUsername()) || auth.username().equals(game.blackUsername())) {
      return false;
    } else {
      return true;
    }
  }

  private void leave(UserGameCommand userGameinfo, Session session) throws IOException {
    AuthData auth = null;
    GameData game = null;

    try {
      auth = authData.getAuthData(userGameinfo.getAuthToken());
      game = gameData.getGame(userGameinfo.getGameID());
    } catch (Exception e) {}

    if (auth == null) {
      var notification = new ServerMessage(ServerMessageType.ERROR);
      notification.setErrorMsg("Error: unauthorized");
      session.getRemote().sendString(new Gson().toJson(notification));
      return;
    } else {
      connections.remove(auth.username());
      var notification = new ServerMessage(ServerMessageType.NOTIFICATION);
      var message = String.format("%s left the game ", auth.username());
      notification.setMsg(message);
      connections.broadcast(auth.username(), notification);
      
      GameData newGameData = game;
      if (auth.username().equals(game.blackUsername())) {
        newGameData = new GameData(userGameinfo.getGameID(), game.whiteUsername(), null, game.gameName(), game.game());
      } else if (auth.username().equals(game.whiteUsername())) {
        newGameData = new GameData(userGameinfo.getGameID(), null, game.blackUsername(), game.gameName(), game.game());
      } 
      try {
        gameData.updateGame(newGameData);
      } catch (Exception e) {}
    }
  }

  private void resign() throws IOException {

  }
}
