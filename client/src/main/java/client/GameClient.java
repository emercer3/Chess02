package client;

import server.ServerFacade;
import exception.ResponseException;
import model.*;
import ui.BoardPrint;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class GameClient {
  private final String serverUrl;
  private final ServerFacade facade;
  private String state;

  public GameClient(String serverUrl) {
    facade = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
    this.state = "signedout";
  }

  public String getState() {
    return state;
  }

  public String eval(String input, String authToken) {
    // try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "leave" -> leaveGame();
        case "quit" -> "quit";
        case "help" -> help();
        default -> help();
      };
    // } catch (ResponseException e) {
    //   return e.getMessage();
    // }
  }

  public String leaveGame() {
    state = "signedin";
    return "Game left";
  }

  public String help() {
    return """
        - 
        - quit
        - help
        """;
  }
}
