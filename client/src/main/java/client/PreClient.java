package client;

import server.ServerFacade;

import java.util.Arrays;

public class PreClient {
  private final String serverUrl;
  private final ServerFacade server;
  
  public PreClient(String serverUrl) {
    server = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
  }

  public String eval(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "register" -> register(params);
        case "login" -> login(params);
        case "quit" -> "quit";
        default -> help();
      };
    } catch (Exception e) {
      return e.getMessage();
    }
  }

public String register(String... params) throws Exception {
  if (params.length >= 1) {
    state = State.REGISTER;
  }

}
  
}
