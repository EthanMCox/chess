package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

  private void addKingMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, Collection<ChessMove> moves) {
    int row = myPosition.getRow();
    int col = myPosition.getColumn();
    ChessPosition[] positions = {
            new ChessPosition(row + 1, col),
            new ChessPosition(row + 1, col + 1),
            new ChessPosition(row + 1, col - 1),
            new ChessPosition(row - 1, col),
            new ChessPosition(row - 1, col + 1),
            new ChessPosition(row - 1, col -1),
            new ChessPosition(row, col + 1),
            new ChessPosition(row, col - 1)
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

  public Collection<ChessMove> kingPieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    // Logic for implementing king moves
    Collection<ChessMove> moves = new ArrayList<>();
    addKingMoves(board, myPosition, pieceColor, moves);
    return moves;
  }
}
