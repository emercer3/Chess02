package client;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class REPL {
  private final PreClient preClient;
  private State state;

  private enum State {
    SIGNEDIN,
    SIGNEDOUT,
    GAMEMODE
  }

  public REPL(String serverUrl) {
    preClient = new PreClient(serverUrl);
    state = State.SIGNEDOUT;
  }

  public void run() {
    System.out.println("Welcome to Online Chess, Sign in or Register to start.");
    // System.out.print(preClient.help());

    Scanner scanner = new Scanner(System.in);
    var result = "";

    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        switch (state) {
          case SIGNEDOUT:
            result = preClient.eval(line);
          case SIGNEDIN:
            // result = postClient.eval(line);
          case GAMEMODE:
            // result = gameClient.eval(line);
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
    System.out.print("\n" + ">>>");
  }
}