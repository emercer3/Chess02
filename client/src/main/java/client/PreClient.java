package client;

import server.ServerFacade;
import exception.ResponseException;

import com.google.gson.Gson;
import java.util.Arrays;
import model.*;

public class PreClient {
  // private String userName = null;
  // private String authToken = null;
  private final String serverUrl;
  private final ServerFacade server;

  
  public enum State {
    SIGNEDIN,
    SIGNEDOUT
  }
  
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
        // case "login" -> login(params);
        case "quit" -> "quit";
        default -> "help"; //help();
      };
    } catch (ResponseException e) {
      return e.getMessage();
    }
  }

public String register(String... params) throws ResponseException {
  if (params.length == 3) {
    UserData userData = new UserData(params[0], params[1], params[2]);

    try {
      server.register(userData);
    } catch (ResponseException e) {
      throw new ResponseException(400, "issue in input, refer to help for sytax");
    }
    return "Logged in as " + userData.username() + ".";
  } 
  throw new ResponseException(400, "Expected: <username> <password> <email>");

}
  
}
