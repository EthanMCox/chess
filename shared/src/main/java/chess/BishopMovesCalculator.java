package chess;

import java.util.Collection;
import java.util.ArrayList;

public class BishopMovesCalculator extends PieceMovesCalculator {

  // Implement logic for boundary checks, piece blocking, capturing pieces, on every diagonal
  public Collection<ChessMove> bishopPieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    // Logic for implementing bishop moves
    Collection<ChessMove> moves = new ArrayList<>();
    addDiagonalMoves(board, myPosition, pieceColor, moves);
    return moves;
  }
}
