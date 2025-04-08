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

  public GameClient(String serverUrl) {
    facade = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
    this.state = "gametime";
  }

  public String getState() {
    return this.state;
  }

  public void setState(String inOrOut) {
    this.state = inOrOut;
  }

  public String eval(String input, String authToken) {
    var tokens = input.toLowerCase().split(" ");
    var cmd = (tokens.length > 0) ? tokens[0] : "help";
    var params = Arrays.copyOfRange(tokens, 1, tokens.length);
    return switch (cmd) {
      case "leavegame" -> leaveGame(authToken);
      case "redrawboard" -> redrawBoard();
      case "makemove" -> makeMove(authToken, params);
      case "resign" -> resign(authToken);
      case "highlightmoves" -> highLightMoves(params);
      case "quit" -> "quit";
      case "help" -> help();
      default -> help();
    };
  }

  public void startWebSocket(String authToken, String color, int gameId) {
    try {
      this.notificationHandler = this;
      this.color = color;
      ws = new WebSocketFacade(serverUrl, notificationHandler);
      ws.joinGame(authToken, gameId);
    } catch (Exception e) {}
  }

  public String leaveGame(String authToken) {
    try {
      // ws = new WebSocketFacade(serverUrl, notificationHandler);
      ws.leaveGame(authToken, gameData.gameID());
    } catch (ResponseException e) {
      //not sure
    }
    this.state = "signedin";
    return "Game left";
  }

  public String redrawBoard() {
    BoardPrint.drawBoard(color, gameData.game(), null);
    return "\n";
  }

  public String makeMove(String authToken, String... params) {
    if (params.length != 5) {
      return "inccorect input, expected <row> <col> <row> <col> <promote to> (first set current, second set new position)";
    }

    ChessPosition start = null;
    ChessPosition newPosition = null;

    try {
      start = new ChessPosition(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
      newPosition = new ChessPosition(Integer.parseInt(params[2]), Integer.parseInt(params[3]));
    } catch (NumberFormatException ex) {
      return "must be integers";
    }

    try {
      ws.makeMove(authToken, gameData.gameID(), new ChessMove(start, newPosition, null));
    } catch (Exception e) {
      return e.getMessage();
    }

    return "\n";
  }

  public String resign(String authToken) {
    try {
      ws.resign(authToken, gameData.gameID());
    } catch (ResponseException e) {
      return e.getMessage();
    }
    return "\n";
  }

  public String highLightMoves(String... params) {
    BoardPrint.drawBoard(color, gameData.game(), new ChessPosition(Integer.parseInt(params[0]), Integer.parseInt(params[1])));
    return "";
  }

  public String help() {
    return """
        - leavegame
        - redrawboard
        - makemove <row> <col> <row> <col> <promote to> (first set current, second set new position)
        - resign
        - highlightmoves <row> <col> (of desired piecee moves to hightlight)
        - help
        """;
  }

  public void notify(ServerMessage serverMsg) {
    if (serverMsg.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
      gameData = serverMsg.getGameData();
      redrawBoard();
      System.out.print("\n" + ">>> ");
    } else if (serverMsg.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)) {
      System.out.print(serverMsg.getServerMsg());
      System.out.print("\n" + ">>> ");
    } else if (serverMsg.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
      System.out.print(serverMsg.getServerErrorMsg());
      System.out.print("\n" + ">>> ");
    }
  }
}
