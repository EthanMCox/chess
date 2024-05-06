package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator{
  public Collection<ChessMove> queenPieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    // Logic for implementing queen moves
    Collection<ChessMove> moves = new ArrayList<>();
    addDiagonalMoves(board, myPosition, pieceColor, moves);
    addStraightMoves(board, myPosition, pieceColor, moves);
    return moves;
  }
}
