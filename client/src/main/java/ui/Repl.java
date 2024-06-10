package ui;

import ui.websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
  private final Client client;

  public Repl(String serverUrl) {
    client = new Client(serverUrl, this);
  }

  public void run() {
    System.out.println(CHESS_LOGO + "Welcome to Chess. Type help to view options");

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        System.out.print(SET_TEXT_COLOR_BLUE + result);
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.print(msg);
      }
      System.out.println();
    }
  }

  private void printPrompt() {
    System.out.print(RESET + ">>> " + SET_TEXT_COLOR_GREEN);
  }

  public void notify(ServerMessage message) {
    // Update this letter with actual notify code
    System.out.println(SET_TEXT_COLOR_RED + message);
    printPrompt();
  }

}
