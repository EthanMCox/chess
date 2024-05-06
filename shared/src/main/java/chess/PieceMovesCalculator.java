package chess;

import java.util.Collection;
import java.util.ArrayList;

public class PieceMovesCalculator {

  protected boolean hasPiece(ChessBoard board, ChessPosition position) {
    return board.getPiece(position) != null;
  }

  protected boolean isOpponentPiece(ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor) {
    ChessPiece piece = board.getPiece(position);
    return piece != null && piece.getTeamColor() != pieceColor;
  }

  protected boolean isInBounds(ChessPosition position) {
    return position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8;
  }
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
    Collection<ChessMove> moves;
    switch (type) {
      case KING:
        KingMovesCalculator kingCalculator = new KingMovesCalculator();
        moves = kingCalculator.pieceMoves(board, myPosition, pieceColor);
        break;
      case QUEEN:
        QueenMovesCalculator queenCalculator = new QueenMovesCalculator();
        moves = queenCalculator.pieceMoves(board, myPosition, pieceColor);
        break;
      case BISHOP:
        BishopMovesCalculator bishopCalculator = new BishopMovesCalculator();
        moves = bishopCalculator.pieceMoves(board, myPosition, pieceColor);
        break;
      case KNIGHT:
        KnightMovesCalculator knightCalculator = new KnightMovesCalculator();
        moves = knightCalculator.pieceMoves(board, myPosition, pieceColor);
        break;
      case ROOK:
        RookMovesCalculator rookCalculator = new RookMovesCalculator();
        moves = rookCalculator.pieceMoves(board, myPosition, pieceColor);
        break;
      case PAWN:
        PawnMovesCalculator pawnCalculator = new PawnMovesCalculator();
        moves = pawnCalculator.pieceMoves(board, myPosition, pieceColor);
        break;
      default:
        moves = new ArrayList<>();
    }
    return moves;
  }
}
