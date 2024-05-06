package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator{
  public Collection<ChessMove> rookPieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    // Logic for implementing rook moves
    Collection<ChessMove> moves = new ArrayList<>();
    addStraightMoves(board, myPosition, pieceColor, moves);
    return moves;
  }
}
