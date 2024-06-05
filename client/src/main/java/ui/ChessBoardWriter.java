package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoardWriter {

  private static final int BOARD_SIZE_IN_SQUARES = 8;
  private static final int SQUARE_SIZE_IN_CHARS = 1;
  private static final String EMPTY = " ";

  public static void main(String[] args) {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    out.print(ERASE_SCREEN);

//    drawHeaders(out);

    drawChessBoard(out, ChessGame.TeamColor.WHITE);

    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
  }



  public static void drawChessBoard(PrintStream out, ChessGame.TeamColor color) {
    printHeadersOrFooters(out, color);

    printHeadersOrFooters(out, color);
  }

  private static void printHeadersOrFooters(PrintStream out, ChessGame.TeamColor color) {
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_WHITE);
    out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS + 1));
    String[] headers = { "a", "b", "c", "d", "e", "f", "g", "h" };
    for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
      if (color == ChessGame.TeamColor.WHITE) {
        out.print(headers[boardCol]);
      } else {
        out.print(headers[BOARD_SIZE_IN_SQUARES - 1 - boardCol]);
      }
      if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
        out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
      }
    }
    out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS + 1));
    out.print(SET_BG_COLOR_BLACK);
    out.println();
  }
}
