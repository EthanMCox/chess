package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {

  private void addPawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, Collection<ChessMove> moves) {
    int row = myPosition.getRow();
    int col = myPosition.getColumn();
  }

  public Collection<ChessMove> pawnPieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    Collection<ChessMove> moves = new ArrayList<>();
    addPawnMoves(board, myPosition, pieceColor, moves);
    return moves;
  }
}
