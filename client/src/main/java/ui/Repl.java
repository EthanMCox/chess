package ui;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
  private final Client client;

  public Repl(String serverUrl) {
    client = new Client(serverUrl);
  }

  public void run() {
    System.out.println("\uD83D\uDC36 Welcome to Chess" + CHESS_LOGO + "Sign in to start.");
  }

}
