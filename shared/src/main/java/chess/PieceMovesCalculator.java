package chess;

import java.util.Collection;
import java.util.ArrayList;

public class PieceMovesCalculator {
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type) {
    Collection<ChessMove> moves;
    switch (type) {
      case KING:
        KingMovesCalculator kingCalculator = new KingMovesCalculator();
        moves = kingCalculator.pieceMoves(board, myPosition);
        break;
      case QUEEN:
        QueenMovesCalculator queenCalculator = new QueenMovesCalculator();
        moves = queenCalculator.pieceMoves(board, myPosition);
        break;
      case BISHOP:
        BishopMovesCalculator bishopCalculator = new BishopMovesCalculator();
        moves = bishopCalculator.pieceMoves(board, myPosition);
        break;
      case KNIGHT:
        KnightMovesCalculator knightCalculator = new KnightMovesCalculator();
        moves = knightCalculator.pieceMoves(board, myPosition);
        break;
      case ROOK:
        RookMovesCalculator rookCalculator = new RookMovesCalculator();
        moves = rookCalculator.pieceMoves(board, myPosition);
        break;
      case PAWN:
        PawnMovesCalculator pawnCalculator = new PawnMovesCalculator();
        moves = pawnCalculator.pieceMoves(board, myPosition);
        break;
      default:
        moves = new ArrayList<>();
    }
    return moves;
  }

}
