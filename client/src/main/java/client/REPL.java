package client;

import java.util.Scanner;

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
            runWebSocket();
            break;
          }
          case "gametime":
            result = gameClient.eval(line, authToken);
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

  private void runWebSocket() {
    if (postClient.runQuestion()) {
      int gameId = postClient.getGameId();
      String playerColor = postClient.getColor();
      gameClient.startWebSocket(authToken, playerColor, gameId);
    }
  }

  private void printPrompt() {
    System.out.print("\n" + ">>> ");
  }
}