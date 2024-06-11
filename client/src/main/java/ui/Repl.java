package ui;

import chess.ChessGame;
import ui.websocket.NotificationHandler;
import websocket.messages.*;

import java.util.Scanner;
import static ui.EscapeSequences.*;
import ui.ChessBoardWriter;

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
    switch (message.getServerMessageType()) {
      case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
      case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
      case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
    }
    // Might need this later, but test
//    printPrompt();
  }

  private void displayNotification(String message) {
    System.out.println(SET_TEXT_COLOR_BLUE + message);
    System.out.println();
  }

  private void displayError(String message) {
    System.out.println(SET_TEXT_COLOR_RED + message);
    System.out.println();
  }

  private void loadGame(ChessGame game) {
    Client.GameRole role = client.getRole();
    client.setGame(game);
    if (role == Client.GameRole.BLACK) {
      ChessBoardWriter.drawChessBoard(System.out, ChessGame.TeamColor.WHITE, game);
    } else {
      ChessBoardWriter.drawChessBoard(System.out, ChessGame.TeamColor.BLACK, game);
    }
    System.out.println();
  }
}
