package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {

  private void addPawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, Collection<ChessMove> moves) {
    int row = myPosition.getRow();
    int col = myPosition.getColumn();
    if (pieceColor == ChessGame.TeamColor.WHITE) {
      // Add moves for white pawns on the second row two spaces away if no piece is in the way
      if (row == 2) {
        ChessPosition frontPosition = new ChessPosition(row + 1, col);
        if (!hasPiece(board, frontPosition)) {
          ChessPosition position = new ChessPosition(row + 2, col);
          if (!hasPiece(board, position)) {
            moves.add(new ChessMove(myPosition, position, null));
          }
        }
      }

      // Add moves directly in front of white pawn
      ChessPosition frontPosition = new ChessPosition(row + 1, col);
      if (!notInBounds(frontPosition)) {
        if (!hasPiece(board, frontPosition)) {
          if (row + 1 == 8) {
            moves.add(new ChessMove(myPosition, frontPosition, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, frontPosition, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(myPosition, frontPosition, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, frontPosition, ChessPiece.PieceType.KNIGHT));
          } else {
            moves.add(new ChessMove(myPosition, frontPosition, null));
          }
        }
      }

      // Add diagonal moves if they exist
      ChessPosition[] positions = {
        new ChessPosition(row + 1, col + 1),
        new ChessPosition(row + 1, col - 1)
      };
      for (ChessPosition position : positions) {
        if (notInBounds(position)) {
          continue;
        }
        if (isOpponentPiece(board, position, pieceColor)) {
          if (row + 1 == 8) {
            moves.add(new ChessMove(myPosition, position, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, position, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(myPosition, position, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, position, ChessPiece.PieceType.KNIGHT));
          } else {
            moves.add(new ChessMove(myPosition, position, null));
          }
        }
      }
    } else { // Piece Color is black
      // Add moves for black pawns on the seventh row two spaces away if no piece is in the way
      if (row == 7) {
        ChessPosition frontPosition = new ChessPosition(row - 1, col);
        if (!hasPiece(board, frontPosition)) {
          ChessPosition position = new ChessPosition(row - 2, col);
          if (!hasPiece(board, position)) {
            moves.add(new ChessMove(myPosition, position, null));
          }
        }
      }

      // Add moves directly in front of black pawn
      ChessPosition frontPosition = new ChessPosition(row - 1, col);
      if (!notInBounds(frontPosition)) {
        if (!hasPiece(board, frontPosition)) {
          if (row - 1 == 1) {
            moves.add(new ChessMove(myPosition, frontPosition, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, frontPosition, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(myPosition, frontPosition, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, frontPosition, ChessPiece.PieceType.KNIGHT));
          } else {
            moves.add(new ChessMove(myPosition, frontPosition, null));
          }
        }
      }

      // Add diagonal moves if they exist
      ChessPosition[] positions = {
              new ChessPosition(row - 1, col + 1),
              new ChessPosition(row - 1, col - 1)
      };
      for (ChessPosition position : positions) {
        if (notInBounds(position)) {
          continue;
        }
        if (isOpponentPiece(board, position, pieceColor)) {
          if (row - 1 == 1) {
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

  public Collection<ChessMove> pawnPieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    Collection<ChessMove> moves = new ArrayList<>();
    addPawnMoves(board, myPosition, pieceColor, moves);
    return moves;
  }
}
