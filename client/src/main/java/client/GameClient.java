package client;

import server.ServerFacade;
import exception.ResponseException;
import model.*;
import chess.*;
import ui.BoardPrint;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.messages.ServerMessage;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class GameClient implements NotificationHandler {
  private final String serverUrl;
  private final ServerFacade facade;
  private NotificationHandler notificationHandler;
  private WebSocketFacade ws;
  private String state;
  private GameData gameData;
  private String color;
  private boolean started;
  private int gameId;

  public GameClient(String serverUrl) {
    facade = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
    this.state = "gametime";
    this.started = false;
  }

  public String getState() {
    return this.state;
  }

  public void setState(String inOrOut) {
    this.state = inOrOut;
  }

  public String eval(String input, String authToken, int gameId) {
    var tokens = input.toLowerCase().split(" ");
    var cmd = (tokens.length > 0) ? tokens[0] : "help";
    var params = Arrays.copyOfRange(tokens, 1, tokens.length);
    if (!started) {
      startWebSocket(authToken, gameId);
      started = true;
    }
    return switch (cmd) {
      case "leavegame" -> leaveGame(authToken);
      case "redrawboard" -> redrawBoard();
      case "makemove" -> makeMove();
      case "resign" -> resign();
      case "highlightmoves" -> highLightMoves();
      case "quit" -> "quit";
      case "help" -> help();
      default -> help();
    };
  }

  public void startWebSocket(String authToken, int gameId) {
    try {
      ws = new WebSocketFacade(serverUrl, notificationHandler);
      ws.joinGame(authToken, gameId);
    } catch (Exception e) {}
  }

  public String leaveGame(String authToken) {
    try {
      ws = new WebSocketFacade(serverUrl, notificationHandler);
      ws.leaveGame(authToken, gameData.gameID());
    } catch (ResponseException e) {
      //not sure
    }
    this.state = "signedin";
    return "Game left";
  }

  public String redrawBoard() {
    BoardPrint.drawBoard(color, gameData.game(), false);
    return "\n";
  }

  public String makeMove() {
    BoardPrint.drawBoard(color, gameData.game(), true);
    return "\n";
  }

  public String resign() {
    return "";
  }

  public String highLightMoves() {
    return "";
  }

  public String help() {
    return """
        - leavegame
        - redrawboard
        - makemove <x> <y> <x> <y> (first set current, second set new position)
        - resign
        - highlightmoves <x> <y> (of desired piecee moves to hightlight)
        - help
        """;
  }

  public void notify(ServerMessage serverMsg) {
    if (serverMsg.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
      gameData = serverMsg.getGameData();
      redrawBoard();
    } else if (serverMsg.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)) {
      System.out.print(serverMsg.getServerMsg());
    } else if (serverMsg.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
      System.out.print(serverMsg.getServerErrorMsg());
    }
  }
}
