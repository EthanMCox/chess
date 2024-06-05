package ui;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
  private final Client client;

  public Repl(String serverUrl) {
    client = new Client(serverUrl);
  }

  public void run() {
    System.out.println(CHESS_LOGO + "\uD83D\uDC36 Welcome to Chess. Type help to view options");

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      String line = scanner.nextLine();
    }
  }

  private void printPrompt() {
    System.out.print(RESET + ">>> " + SET_TEXT_COLOR_GREEN);
  }

}
