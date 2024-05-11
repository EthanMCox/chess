package chess;

import java.util.Collection;
import java.util.ArrayList;

public class PieceMovesCalculator {

  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
    Collection<ChessMove> moves;
    switch (type) {
      case KING:
        KingMovesCalculator kingCalculator = new KingMovesCalculator();
        moves = kingCalculator.kingPieceMoves(board, myPosition, pieceColor);
        break;
      case QUEEN:
        QueenMovesCalculator queenCalculator = new QueenMovesCalculator();
        moves = queenCalculator.queenPieceMoves(board, myPosition, pieceColor);
        break;
      case BISHOP:
        BishopMovesCalculator bishopCalculator = new BishopMovesCalculator();
        moves = bishopCalculator.bishopPieceMoves(board, myPosition, pieceColor);
        break;
      case KNIGHT:
        KnightMovesCalculator knightCalculator = new KnightMovesCalculator();
        moves = knightCalculator.knightPieceMoves(board, myPosition, pieceColor);
        break;
      case ROOK:
        RookMovesCalculator rookCalculator = new RookMovesCalculator();
        moves = rookCalculator.rookPieceMoves(board, myPosition, pieceColor);
        break;
      case PAWN:
        PawnMovesCalculator pawnCalculator = new PawnMovesCalculator();
        moves = pawnCalculator.pawnPieceMoves(board, myPosition, pieceColor);
        break;
      default:
        moves = new ArrayList<>();
    }
    return moves;
  }

  protected void addDiagonalMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, Collection<ChessMove> moves) {
    int row = myPosition.getRow();
    int col = myPosition.getColumn();

    // Check for moves in the top right diagonal
    for (int i = 1; i < 8; i++) {
      ChessPosition position = new ChessPosition(row + i, col + i);
      if (notInBounds(position)) {
        break;
      }
      if (hasPiece(board, position)) {
        if (isOpponentPiece(board, position, pieceColor)) {
          moves.add(new ChessMove(myPosition, position, null));
        }
        break;
      }
      moves.add(new ChessMove(myPosition, position, null));
    }

    // Check for moves in the top left diagonal
    for (int i = 1; i < 8; i++) {
      ChessPosition position = new ChessPosition(row + i, col - i);
      if (notInBounds(position)) {
        break;
      }
      if (hasPiece(board, position)) {
        if (isOpponentPiece(board, position, pieceColor)) {
          moves.add(new ChessMove(myPosition, position, null));
        }
        break;
      }
      moves.add(new ChessMove(myPosition, position, null));
    }

    // Check for moves in the bottom right diagonal
    for (int i = 1; i < 8; i++) {
      ChessPosition position = new ChessPosition(row - i, col + i);
      if (notInBounds(position)) {
        break;
      }
      if (hasPiece(board, position)) {
        if (isOpponentPiece(board, position, pieceColor)) {
          moves.add(new ChessMove(myPosition, position, null));
        }
        break;
      }
      moves.add(new ChessMove(myPosition, position, null));
    }

    // Check for moves in the bottom left diagonal
    for (int i = 1; i < 8; i++) {
      ChessPosition position = new ChessPosition(row - i, col - i);
      if (notInBounds(position)) {
        break;
      }
      if (hasPiece(board, position)) {
        if (isOpponentPiece(board, position, pieceColor)) {
          moves.add(new ChessMove(myPosition, position, null));
        }
        break;
      }
      moves.add(new ChessMove(myPosition, position, null));
    }
  }

  protected void addStraightMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, Collection<ChessMove> moves) {
    int row = myPosition.getRow();
    int col = myPosition.getColumn();

    // Check for moves above
    for (int i = 1; i < 8; i++) {
      ChessPosition position = new ChessPosition(row + i, col);
      if (notInBounds(position)) {
        break;
      }
      if (hasPiece(board, position)) {
        if (isOpponentPiece(board, position, pieceColor)) {
          moves.add(new ChessMove(myPosition, position, null));
        }
        break;
      }
      moves.add(new ChessMove(myPosition, position, null));
    }

    // Check for moves below
    for (int i = 1; i < 8; i++) {
      ChessPosition position = new ChessPosition(row - i, col);
      if (notInBounds(position)) {
        break;
      }
      if (hasPiece(board, position)) {
        if (isOpponentPiece(board, position, pieceColor)) {
          moves.add(new ChessMove(myPosition, position, null));
        }
        break;
      }
      moves.add(new ChessMove(myPosition, position, null));
    }

    // Check for moves to the right
    for (int i = 1; i < 8; i++) {
      ChessPosition position = new ChessPosition(row, col + i);
      if (notInBounds(position)) {
        break;
      }
      if (hasPiece(board, position)) {
        if (isOpponentPiece(board, position, pieceColor)) {
          moves.add(new ChessMove(myPosition, position, null));
        }
        break;
      }
      moves.add(new ChessMove(myPosition, position, null));
    }

    // Check for moves to the left
    for (int i = 1; i < 8; i++) {
      ChessPosition position = new ChessPosition(row, col - i);
      if (notInBounds(position)) {
        break;
      }
      if (hasPiece(board, position)) {
        if (isOpponentPiece(board, position, pieceColor)) {
          moves.add(new ChessMove(myPosition, position, null));
        }
        break;
      }
      moves.add(new ChessMove(myPosition, position, null));
    }
  }

  protected void addKnightOrKingMovesFromPositions(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor, Collection<ChessMove> moves, ChessPosition[] positions) {
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

  protected boolean hasPiece(ChessBoard board, ChessPosition position) {
    return board.getPiece(position) != null;
  }

  protected boolean isOpponentPiece(ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor) {
    ChessPiece piece = board.getPiece(position);
    return piece != null && piece.getTeamColor() != pieceColor;
  }

  protected boolean notInBounds(ChessPosition position) {
    return position.getRow() < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8;
  }
}
