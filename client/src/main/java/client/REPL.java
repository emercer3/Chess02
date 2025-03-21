package client;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class REPL {
  private final PreClient preClient;
  private final PostClient postClient;
  private String state;
  private String authToken;

  public REPL(String serverUrl) {
    preClient = new PreClient(serverUrl);
    postClient = new PostClient(serverUrl);
    state = "signedout";
  }

  public void run() {
    System.out.println("Welcome to Online Chess, Sign in or Register to start.");
    System.out.print(preClient.help());

    Scanner scanner = new Scanner(System.in);
    var result = "";

    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        switch (state) {
          case "signedout": {
            result = preClient.eval(line);
            state = preClient.getState();
            authToken = preClient.getAuthToken();
            break;
          } case "signedin": {
            result = postClient.eval(line, authToken);
            state = postClient.getState();
            break;
          } case "gametime":
            // result = gameClient.eval(line);
            // break;
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