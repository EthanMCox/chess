package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoardWriter {

  private static final int BOARD_SIZE_IN_SQUARES = 8;
  private static final int SQUARE_SIZE_IN_CHARS = 3;
  private static final String EMPTY = " ";

  public static void main(String[] args) {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    out.print(ERASE_SCREEN);

//    drawHeaders(out);

    drawChessBoard(out, ChessGame.TeamColor.WHITE, new ChessBoard());

    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
  }



  public static void drawChessBoard(PrintStream out, ChessGame.TeamColor color, ChessBoard board) {
    printHeadersOrFooters(out, color);
    ChessPiece[][] squares = board.getSquares();
    drawRows(out, color, squares);

    printHeadersOrFooters(out, color);
  }

  private static void printHeadersOrFooters(PrintStream out, ChessGame.TeamColor color) {
    setBorder(out);
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

  private static void drawRows(PrintStream out, ChessGame.TeamColor color, ChessPiece[][] squares) {
    for (int row = 0; row < BOARD_SIZE_IN_SQUARES; row++) {
      drawRow(out, row, color, squares);
    }
  }

  private static void drawRow(PrintStream out, int row, ChessGame.TeamColor color, ChessPiece[][] squares) {
    setBorder(out);
    out.print(EMPTY);
    int rowNumber = color == ChessGame.TeamColor.BLACK ? row + 1 : BOARD_SIZE_IN_SQUARES - row;
    out.print(rowNumber);
    out.print(EMPTY);
    ChessGame.TeamColor squareColor;
    squareColor = color == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

    for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {
      squareColor = squareColor == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
//      drawSquare(out, squares[row][col], row, col);
    }
    out.print(EMPTY);
    out.print(rowNumber);
    out.print(EMPTY);
    out.print(SET_BG_COLOR_BLACK);
    out.println();

  }

  private static void drawSquare(PrintStream out, ChessPiece piece, int row, int col, ChessGame.TeamColor squareColor) {
    if (squareColor == ChessGame.TeamColor.WHITE) {
      out.print(SET_BG_COLOR_WHITE);
    } else {
      out.print(SET_BG_COLOR_BLACK);
    }
    if (piece == null) {
      out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
    } else {
      out.print(EMPTY);
      drawPiece(out, piece);
      out.print(EMPTY);
    }
  }

  private static void setBorder(PrintStream out) {
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_BLACK);
    out.print(SET_TEXT_BOLD);
  }


}
