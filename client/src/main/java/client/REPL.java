package client;

import java.util.Scanner;
import static ui.EscapeSequences.*;

import websocket.*;
import websocket.messages.ServerMessage;
import websocket.NotificationHandler;

public class REPL {
  private final PreClient preClient;
  private final PostClient postClient;
  private final GameClient gameClient;
  private String state;
  private String authToken;

  public REPL(String serverUrl) {
    preClient = new PreClient(serverUrl);
    postClient = new PostClient(serverUrl);
    gameClient = new GameClient(serverUrl);
    state = "signedout";
  }

  public void run() {
    System.out.println("Welcome to Online Chess, Sign in or Register to start.");
    System.out.print(preClient.prehelp());

    Scanner scanner = new Scanner(System.in);
    var result = "";

    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        switch (state) {
          case "signedout": {
            result = preClient.eval(line);
            this.state = preClient.getState();
            authToken = preClient.getAuthToken();
            preClient.setState("signedout");
            break;
          }
          case "signedin": {
            result = postClient.eval(line, authToken);
            this.state = postClient.getState();
            postClient.setState("signedin");
            break;
          }
          case "gametime":
            int gameId = postClient.getGameId();
            result = gameClient.eval(line, authToken, gameId);
            this.state = gameClient.getState();
            gameClient.setState("gametime");
            break;
        }
        System.out.print(result);
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.print(msg);
      }
    }
    System.out.println();
  }

  private void printPrompt() {
    System.out.print("\n" + ">>> ");
  }
}