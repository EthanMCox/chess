package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {

  private enum Direction {
    UP,
    DOWN
  }

  public Collection<ChessMove> pawnPieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    Collection<ChessMove> moves = new ArrayList<>();
    addPawnMoves(board, myPosition, pieceColor, moves);
    return moves;
  }

  private void addPawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, Collection<ChessMove> moves) {
    int row = myPosition.getRow();
    int col = myPosition.getColumn();
    Direction direction;
    int startRow;
    int promotionRow;
    if (pieceColor == ChessGame.TeamColor.WHITE) {
      direction = Direction.UP;
      startRow = 2;
      promotionRow = 8;
    } else {
      direction = Direction.DOWN;
      startRow = 7;
      promotionRow = 1;
    }
    addPawnDoubleMove(board, myPosition, moves, row, col, startRow, direction);
    addPawnFrontMove(board, myPosition, moves, row, col, promotionRow, direction);
    addDiagonalCaptureMoves(board, myPosition, pieceColor, moves, row, col, promotionRow, direction);
  }

  private void addPawnDoubleMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col, int startRow, Direction direction) {
    if (row == startRow) {
      ChessPosition frontPosition = (direction == Direction.UP) ? new ChessPosition(row + 1, col) : new ChessPosition(row - 1, col);
      if (!hasPiece(board, frontPosition)) {
        ChessPosition position = (direction == Direction.UP) ? new ChessPosition(row + 2, col) : new ChessPosition(row - 2, col);
        if (!hasPiece(board, position)) {
          moves.add(new ChessMove(myPosition, position, null));
        }
      }
    }
  }

  private void addPawnFrontMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col, int promotionRow, Direction direction) {
    ChessPosition frontPosition = (direction == Direction.UP) ? new ChessPosition(row + 1, col) : new ChessPosition(row - 1, col);
    if (!notInBounds(frontPosition)) {
      if (!hasPiece(board, frontPosition)) {
        int targetRow = (direction == Direction.UP) ? row + 1 : row - 1;
        if (targetRow == promotionRow) {
          moves.add(new ChessMove(myPosition, frontPosition, ChessPiece.PieceType.QUEEN));
          moves.add(new ChessMove(myPosition, frontPosition, ChessPiece.PieceType.ROOK));
          moves.add(new ChessMove(myPosition, frontPosition, ChessPiece.PieceType.BISHOP));
          moves.add(new ChessMove(myPosition, frontPosition, ChessPiece.PieceType.KNIGHT));
        } else {
          moves.add(new ChessMove(myPosition, frontPosition, null));
        }
      }
    }
  }

  private void addDiagonalCaptureMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, Collection<ChessMove> moves, int row, int col, int promotionRow, Direction direction) {
    int targetRow = (direction == Direction.UP) ? row + 1 : row - 1;
    ChessPosition[] positions = {
            new ChessPosition(targetRow, col + 1),
            new ChessPosition(targetRow, col - 1)
    };
    for (ChessPosition position : positions) {
      if (notInBounds(position)) {
        continue;
      }
      if (isOpponentPiece(board, position, pieceColor)) {
        if (targetRow == promotionRow) {
          moves.add(new ChessMove(myPosition, position, ChessPiece.PieceType.QUEEN));
          moves.add(new ChessMove(myPosition, position, ChessPiece.PieceType.ROOK));
          moves.add(new ChessMove(myPosition, position, ChessPiece.PieceType.BISHOP));
          moves.add(new ChessMove(myPosition, position, ChessPiece.PieceType.KNIGHT));
        } else {
          moves.add(new ChessMove(myPosition, position, null));
        }
      }
    }
  }
}
