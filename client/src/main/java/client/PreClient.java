package client;

import server.ServerFacade;
import exception.ResponseException;

import com.google.gson.Gson;
import java.util.Arrays;
import model.*;

public class PreClient {
  private final String serverUrl;
  private final ServerFacade facade;
  private String state;
  private String authToken;

  
  // public enum State {
  //   SIGNEDIN,
  //   SIGNEDOUT
  // }
  
  public PreClient(String serverUrl) {
    facade = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
    this.state = "signedout";
    this.authToken = null;
  }

  public String getState() {
    return state;
  }

  public String getAuthToken() {
    return authToken;
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
        case "help" -> help();
        default -> help();
      };
    } catch (ResponseException e) {
      return e.getMessage();
    }
  }

  public String register(String... params) throws ResponseException {
    AuthData authData;

    if (params.length == 3) {
      UserData userData = new UserData(params[0], params[1], params[2]);

      try {
        authData = facade.register(userData);
      } catch (ResponseException e) {
        if (e.getMessage().equals("Error: already taken")) {
          throw new ResponseException(403, "username is already taken");
        } else {
          throw new ResponseException(500, "issue in input, refer to help for sytax");
        }
      }
      state = "signedin";
      authToken = authData.authToken();
      return "Logged in as " + userData.username() + ".";
    } 
    throw new ResponseException(400, "Expected: <username> <password> <email>");

  }

  public String login(String... params) throws ResponseException {
    AuthData authData;

    if (params.length == 2) {
      String userName = params[0];
      String password = params[1];

      try {
        authData = facade.login(userName, password);
      } catch (ResponseException e) {
        if (e.getMessage().equals("Error: unauthorized")) {
          throw new ResponseException(401, "inccorect username or pasword");
        } else {
          throw new ResponseException(500, "issue in input, refer to help for sytax");
        }
      }
      state = "signedin";
      authToken = authData.authToken();
      return "Logged in as " + userName + ".";
    }
    throw new ResponseException(500, "Expected: <username> <password>");
  }

  public String help() {
    return """
        - resgister <username> <password> <email>
        - login <username> <password>
        - quit
        - help
        """;
  }
  
}
