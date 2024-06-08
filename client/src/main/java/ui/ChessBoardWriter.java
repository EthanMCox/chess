package ui;

import chess.*;
import exception.ExceptionResult;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class ChessBoardWriter {

  private static final int BOARD_SIZE_IN_SQUARES = 8;
  private static final int SQUARE_SIZE_IN_CHARS = 3;
  private static final String EMPTY = " ";

  public static void main(String[] args) throws ExceptionResult {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    out.print(ERASE_SCREEN);
    ChessGame game = new ChessGame();
    drawChessBoard(out, ChessGame.TeamColor.WHITE, game, new ChessPosition("b1"));

    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  public static void drawChessBoard(PrintStream out, ChessGame.TeamColor color, ChessBoard board) {
    ChessGame game = new ChessGame();
    game.setBoard(board);
    drawChessBoard(out, color, game);
  }

  public static void drawChessBoard(PrintStream out, ChessGame.TeamColor color, ChessGame game) {
    drawChessBoard(out, color, game, null);
  }

  public static void drawChessBoard(PrintStream out, ChessGame.TeamColor color, ChessGame game, ChessPosition position) {
    ChessBoard board = game.getBoard();
    Collection<ChessMove> validMoves = null;
    if (position != null) {
      validMoves = getValidMoves(position, game);
    }

    printHeadersOrFooters(out, color);

    ChessPiece[][] squares = board.getSquares();
    drawRows(out, color, squares, validMoves);

    printHeadersOrFooters(out, color);
  }

  private static Collection<ChessMove> getValidMoves(ChessPosition position, ChessGame game) {
    return game.validMoves(position);
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
        out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS-1));
      }
    }
    out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS + 1));
    out.print(SET_BG_COLOR_BLACK);
    out.println();
  }

  private static void drawRows(PrintStream out, ChessGame.TeamColor color, ChessPiece[][] squares, Collection<ChessMove> validMoves) {
    ChessGame.TeamColor squareColor;
    squareColor = ChessGame.TeamColor.WHITE;
    for (int row = 0; row < BOARD_SIZE_IN_SQUARES; row++) {
      squareColor = squareColor == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
      drawRow(out, row, color, squareColor, squares, validMoves);
    }
  }

  private static void drawRow(PrintStream out, int row, ChessGame.TeamColor color, ChessGame.TeamColor squareColor ,ChessPiece[][] squares, Collection<ChessMove> validMoves) {
    setBorder(out);
    out.print(EMPTY);
    int rowNumber = color == ChessGame.TeamColor.BLACK ? row + 1 : BOARD_SIZE_IN_SQUARES - row;
    out.print(rowNumber);
    out.print(EMPTY);

    for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {
      squareColor = squareColor == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
      ChessPiece piece;
      boolean validMove = false;
      ChessPosition position;
      if (color == ChessGame.TeamColor.BLACK) {
        piece = squares[row][BOARD_SIZE_IN_SQUARES -1 - col];
        position = new ChessPosition(row + 1, BOARD_SIZE_IN_SQUARES - col);
      } else {
        piece = squares[BOARD_SIZE_IN_SQUARES - 1 - row][col];
        position = new ChessPosition(BOARD_SIZE_IN_SQUARES - row, col + 1);
      }
      if (validMoves != null) {
        for (ChessMove move : validMoves) {
          if (move.getEndPosition().equals(position)) {
            validMove = true;
            break;
          }
        }
      }

      drawSquare(out, piece, squareColor, validMove);
    }
    setBorder(out);
    out.print(EMPTY);
    out.print(rowNumber);
    out.print(EMPTY);
    out.print(SET_BG_COLOR_BLACK);
    out.println();

  }

  private static void drawSquare(PrintStream out, ChessPiece piece, ChessGame.TeamColor squareColor, boolean validMove) {
    if (squareColor == ChessGame.TeamColor.WHITE) {
      if (validMove) {
        out.print(SET_BG_COLOR_GREEN);
      } else {
        out.print(SET_BG_COLOR_WHITE);
      }
    } else {
      if (validMove) {
        out.print(SET_BG_COLOR_DARK_GREEN);
      } else {
        out.print(SET_BG_COLOR_BLACK);
      }
    }
    if (piece == null) {
      out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
    } else {
      drawPiece(out, piece, validMove);
    }
  }

  private static void drawPiece(PrintStream out, ChessPiece piece, boolean validMove) {
    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
      out.print(SET_TEXT_COLOR_RED);
    } else {
      out.print(SET_TEXT_COLOR_BLUE);
    }
    if (validMove) {
      out.print(SET_TEXT_COLOR_BLACK);
    }
    switch (piece.getTeamColor()) {
      case WHITE -> {
        switch (piece.getPieceType()) {
          case KING -> out.print(WHITE_KING);
          case QUEEN -> out.print(WHITE_QUEEN);
          case BISHOP -> out.print(WHITE_BISHOP);
          case KNIGHT -> out.print(WHITE_KNIGHT);
          case ROOK -> out.print(WHITE_ROOK);
          case PAWN -> out.print(WHITE_PAWN);
        }
      }
      case BLACK -> {
        switch (piece.getPieceType()) {
          case KING -> out.print(BLACK_KING);
          case QUEEN -> out.print(BLACK_QUEEN);
          case BISHOP -> out.print(BLACK_BISHOP);
          case KNIGHT -> out.print(BLACK_KNIGHT);
          case ROOK -> out.print(BLACK_ROOK);
          case PAWN -> out.print(BLACK_PAWN);
        }
      }
    }
  }

  private static void setBorder(PrintStream out) {
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_BLACK);
    out.print(SET_TEXT_BOLD);
  }


}
