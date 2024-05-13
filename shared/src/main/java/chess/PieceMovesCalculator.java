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

  public void handleEnPassant(ChessBoard board, ChessBoard previousBoardState, ChessGame.TeamColor teamTurn, ChessPosition startPosition, Collection<ChessMove> possibleMoves, ChessPiece piece) {
    if (piece == null || piece.getPieceType() != ChessPiece.PieceType.PAWN) {
      return;
    }
    ChessGame.TeamColor pieceColor = piece.getTeamColor();
    int row = startPosition.getRow();
    int direction;
    if (row == 5 && piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
      direction = 1;
    }
    else if (row == 4 && piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
      direction = -1;
    }
    else {
      return;
    }
    int col = startPosition.getColumn();
    ChessPosition leftPosition = new ChessPosition(row, col - 1);
    ChessPosition rightPosition = new ChessPosition(row, col + 1);
    checkEnPassantSide(board, previousBoardState, teamTurn, startPosition, possibleMoves, piece, pieceColor, row, col, direction, -1, leftPosition);
    checkEnPassantSide(board, previousBoardState, teamTurn, startPosition, possibleMoves, piece, pieceColor, row, col, direction, 1, rightPosition);
  }

  private void checkEnPassantSide(ChessBoard board, ChessBoard previousBoardState, ChessGame.TeamColor teamTurn, ChessPosition startPosition, Collection<ChessMove> possibleMoves, ChessPiece piece, ChessGame.TeamColor pieceColor, int row, int col, int rowDirection, int colDirection, ChessPosition sidePosition){
    if (notInBounds(sidePosition)) {
      return;
    }
    ChessPiece sidePiece = board.getPiece(sidePosition);

    if ((sidePiece != null && sidePiece.getPieceType() == ChessPiece.PieceType.PAWN) && (sidePiece.getTeamColor() != pieceColor) && enPassantIsValid(board, previousBoardState, teamTurn, row, col, rowDirection, col + colDirection)) {
      addEnPassantMove(startPosition, possibleMoves, row, rowDirection, col + colDirection);
    }
  }

  private boolean enPassantIsValid(ChessBoard board, ChessBoard previousBoardState, ChessGame.TeamColor teamTurn, int row, int column, int direction, int targetColumn) {
    ChessPosition diagonalPosition = new ChessPosition(row + direction, targetColumn);
    ChessPosition twoSpacesForward = new ChessPosition(row + 2 * direction, targetColumn);
    if (board.getPiece(diagonalPosition) != null) {
      return false;
    }

    ChessPiece oldPiece = previousBoardState.getPiece(twoSpacesForward);

    return oldPiece != null && oldPiece.getPieceType() == ChessPiece.PieceType.PAWN && oldPiece.getTeamColor() != teamTurn && board.getPiece(twoSpacesForward) == null;
  }

  private void addEnPassantMove(ChessPosition startPosition, Collection<ChessMove> possibleMoves, int row, int direction, int targetColumn) {
    ChessPosition endPosition = new ChessPosition(row + direction, targetColumn);
    possibleMoves.add(new ChessMove(startPosition, endPosition, null));
  }
}
