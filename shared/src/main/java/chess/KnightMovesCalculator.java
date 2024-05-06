package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator{
  private void addKnightMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, Collection<ChessMove> moves) {
    int row = myPosition.getRow();
    int col = myPosition.getColumn();
    ChessPosition[] positions = {
      new ChessPosition(row + 2, col + 1),
      new ChessPosition(row + 2, col - 1),
      new ChessPosition(row - 2, col + 1),
      new ChessPosition(row - 2, col - 1),
      new ChessPosition(row + 1, col + 2),
      new ChessPosition(row + 1, col - 2),
      new ChessPosition(row - 1, col + 2),
      new ChessPosition(row - 1, col - 2)
    };

    for (ChessPosition position : positions) {
      if (notInBounds(position)) {
        continue;
      }
      if (hasPiece(board, position)) {
        if (isOpponentPiece(board, position, pieceColor)) {
          moves.add(new ChessMove(myPosition, position, null));
        }
      } else {
        moves.add(new ChessMove(myPosition, position, null));
      }
    }
  }
  public Collection<ChessMove> knightPieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    // Logic for implementing knight moves
    Collection<ChessMove> moves = new ArrayList<>();
    addKnightMoves(board, myPosition, pieceColor, moves);
    return moves;
  }
}
