package client;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class REPL {
  private final PreClient preClient;

  public REPL(String serverUrl) {
    preClient = new PreClient(serverUrl);
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
        result = preClient.eval(line);
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