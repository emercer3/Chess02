package client;

import server.ServerFacade;
import exception.ResponseException;

import com.google.gson.Gson;
import java.util.Arrays;
import model.*;

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

  public String eval(String input) {
    // try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "quit" -> "quit";
        case "help" -> help();
        default -> help();
      };
    // } catch (ResponseException e) {
    //   return e.getMessage();
    // }
  }

  public String help() {
    return """
        - 
        - quit
        - help
        """;
  }
}
