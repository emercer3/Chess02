package client;

import server.ServerFacade;
import server.ServerFacade.GameName;
import ui.BoardPrint;
import exception.ResponseException;

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import model.*;

public class PostClient {
  private final String serverUrl;
  private final ServerFacade facade;
  private String state;
  private final HashMap<Integer, Integer> gameIds = new HashMap<>();

  public PostClient(String serverUrl) {
    facade = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
    this.state = "signedin";
  }

  public String getState() {
    return state;
  }

  public String eval(String input, String authToken) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "logout" -> logout(authToken);
        case "creategame" -> createGame(authToken, params);
        case "listgames" -> listGames(authToken);
        case "joingame" -> joinGame(authToken, params);
        case "observegame" -> observeGame(authToken, params);
        case "quit" -> "quit";
        case "help" -> help();
        default -> help();
      };
    } catch (ResponseException e) {
      return e.getMessage();
    }
  }

  public String logout(String authToken) throws ResponseException {
    try {
      facade.logout(authToken);
    } catch (ResponseException e) {
      throw new ResponseException(500, e.getMessage());
    }
    state = "signedout";
    return "Successfully logged out.";
  }

  public String createGame(String authToken, String... params) throws ResponseException {
    if (params.length == 1) {
      String gameName = params[0];
      int gameId;

      try {
        gameId = facade.createGame(authToken, gameName);
      } catch (ResponseException e) {
        throw new ResponseException(500, "issue in input, refer to help for sytax");
      }
      return "game Successfully created, gameID = " + gameId + ".";
    }
    throw new ResponseException(500, "expected: <gamename>");
  }

  public String listGames(String authToken) throws ResponseException {
    Collection<GameSummaryData> gameList;

    try {
      gameList = facade.listGames(authToken);
    } catch (ResponseException e) {
      throw new ResponseException(500, "no games to list");
    }

    if (gameList.isEmpty()) {
      return "No games available";
    }
    int numbering = 1;

    StringBuilder result = new StringBuilder("Available Games:\n");
    for (GameSummaryData game : gameList) {
      gameIds.put(numbering, game.gameID());
      result.append("- Game " + numbering + ": ")
          .append(", Name: ").append(game.gameName())
          .append(", White: ").append(game.whiteUsername())
          .append(", Black: ").append(game.blackUsername())
          .append("\n");
      numbering++;
    }

    return result.toString();
  }

  public String joinGame(String authToken, String... params) throws ResponseException {
    if (params.length == 2) {
      String playerColor = params[0];
      var gameNumber = Integer.parseInt(params[1]);
      int gameId = gameIds.get(gameNumber);

      try {
        facade.joinGame(authToken, playerColor, gameId);
        BoardPrint.drawBoard(playerColor);
      } catch (ResponseException e) {
        String msg;
        if (e.getMessage().equals("Error: bad request")) {
          throw new ResponseException(400, "not valid gameId");
        } else if (e.getMessage().equals("Error: already taken")) {
          throw new ResponseException(403, "Color is already taken, choose other");
        } else {
          throw new ResponseException(500, "issue in input, refer to help for sytax");
        }
      }
      state = "gametime";
      return "Successfully joined game as " + playerColor;
    }

    return "inccorect number of inputs, please refer to help";
  }

  public String observeGame(String authToken, String... params) {
    BoardPrint.drawBoard(params[0]);
    state = "gametime";
    return "Observing the game...";
  }

  public String help() {
    return """
        - logout
        - creategame <gamename>
        - listgames
        - joingame <Color(WHITE/BLACK)> <gameID>
        - observegame <gameID> <color>
        - help
        """;
  }
}
