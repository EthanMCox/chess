package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

    private ChessBoard previousBoardState;

    private boolean whiteKingMoved;
    private boolean blackKingMoved;
    private boolean whiteRookLeftMoved;
    private boolean whiteRookRightMoved;
    private boolean blackRookLeftMoved;
    private boolean blackRookRightMoved;

    public static final Collection<ChessPosition> VALID_POSITIONS;

    static {
        VALID_POSITIONS = new HashSet<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                VALID_POSITIONS.add(new ChessPosition(i, j));
            }
        }
    }
    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        previousBoardState = new ChessBoard(board);
        previousBoardState.resetBoard();
        teamTurn = TeamColor.WHITE;
    }


    public ChessGame(ChessBoard board, TeamColor teamTurn) {
        this.board = new ChessBoard(board);
        setTeamTurn(teamTurn);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", teamTurn=" + teamTurn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return whiteKingMoved == chessGame.whiteKingMoved && blackKingMoved == chessGame.blackKingMoved && whiteRookLeftMoved == chessGame.whiteRookLeftMoved && whiteRookRightMoved == chessGame.whiteRookRightMoved && blackRookLeftMoved == chessGame.blackRookLeftMoved && blackRookRightMoved == chessGame.blackRookRightMoved && Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn && Objects.equals(previousBoardState, chessGame.previousBoardState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn, previousBoardState, whiteKingMoved, blackKingMoved, whiteRookLeftMoved, whiteRookRightMoved, blackRookLeftMoved, blackRookRightMoved);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        ChessPiece.addEnPassantMoves(board, previousBoardState, teamTurn, startPosition, possibleMoves, piece);
        addCastlingMoves(startPosition, possibleMoves, piece);
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessGame.TeamColor pieceTeamTurn = piece.getTeamColor();
        for (ChessMove move : possibleMoves) {
            ChessGame forecastedGame = new ChessGame(new ChessBoard(board), pieceTeamTurn);
            ChessBoard newBoard = forecastedGame.getBoard();

            newBoard.movePiece(move);
            // Check if king is in check after making a move
            if (!forecastedGame.isInCheck(pieceTeamTurn)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    private void addCastlingMoves(ChessPosition startPosition, Collection<ChessMove> possibleMoves, ChessPiece piece) {
        if (piece.getPieceType() != ChessPiece.PieceType.KING) {
            return;
        }
        TeamColor pieceColor = piece.getTeamColor();
        if (pieceColor == TeamColor.WHITE) {
            if (!whiteKingMoved) {
                if (!whiteRookLeftMoved) {
                    ChessPosition leftRookPosition = new ChessPosition(1, 1);
                    handleCastling(board, startPosition, leftRookPosition, possibleMoves);
                }
                if (!whiteRookRightMoved) {
                    ChessPosition rightRookPosition = new ChessPosition(1, 8);
                    handleCastling(board, startPosition, rightRookPosition, possibleMoves);
                }
            }
        } else {
            if (!blackKingMoved) {
                if (!blackRookLeftMoved) {
                    ChessPosition leftRookPosition = new ChessPosition(8, 1);
                    handleCastling(board, startPosition, leftRookPosition, possibleMoves);
                }
                if (!blackRookRightMoved) {
                    ChessPosition rightRookPosition = new ChessPosition(8, 8);
                    handleCastling(board, startPosition, rightRookPosition, possibleMoves);
                }
            }
        }
    }

    private void handleCastling(ChessBoard board, ChessPosition kingPosition, ChessPosition rookPosition, Collection<ChessMove> possibleMoves) {
        // Handle checking pieces if any pieces are between the king and the rook
        int row = kingPosition.getRow();
        int kingCol = kingPosition.getColumn();
        int rookCol = rookPosition.getColumn();

        int direction = rookCol - kingCol > 0 ? 1 : -1;
        for (int i = 1; kingCol + i * direction != rookCol; i++) {
            ChessPosition position = new ChessPosition(row, kingCol + i * direction);
            if (board.getPiece(position) != null) {
                return;
            }
            if (Math.abs(direction * i) <= 2 && piecePositionInDanger(teamTurn, position)) {
                return;
            }
        }
        ChessPosition newKingPosition = new ChessPosition(row, kingCol + 2 * direction);
        possibleMoves.add(new ChessMove(kingPosition, newKingPosition, null));
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        if (startPosition == null || !VALID_POSITIONS.contains(startPosition)) {
            throw new InvalidMoveException("Invalid start position");
        } else if (board.getPiece(startPosition) == null){
            throw new InvalidMoveException("No piece at start position");
        } else if (!validMoves(startPosition).contains(move)) {
            throw new InvalidMoveException("Invalid move");
        }  else if (startPosition == endPosition) {
            throw new InvalidMoveException("Start and end positions are the same");
        } else if (!board.getPiece(startPosition).getTeamColor().equals(teamTurn)) {
            throw new InvalidMoveException("Not your turn");
        }
        else {
            previousBoardState = new ChessBoard(board);
            board.movePiece(move);

            int startRow = startPosition.getRow();
            int startCol = startPosition.getColumn();
            int endRow = endPosition.getRow();
            int endCol = endPosition.getColumn();
            updateCastlingStates(startRow, startCol, endRow, endCol);

            teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        }
    }

    private void updateCastlingStates(int startRow, int startCol, int endRow, int endCol) {
        if ((startRow == 1 && startCol == 1) || (endRow == 1 && endCol == 1)) {
            whiteRookLeftMoved = true;
        } else if ((startRow == 1 && startCol == 8) || (endRow == 1 && endCol == 8)) {
            whiteRookRightMoved = true;
        } else if ((startRow == 8 && startCol == 1) || (endRow == 8 && endCol == 1)) {
            blackRookLeftMoved = true;
        } else if ((startRow == 8 && startCol == 8) || (endRow == 8 && endCol == 8)) {
            blackRookRightMoved = true;
        } else if ((startRow == 1 && startCol == 5) || (endRow == 1 && endCol == 5)) {
            whiteKingMoved = true;
        } else if ((startRow == 8 && startCol == 5) || (endRow == 8 && endCol == 5)) {
            blackKingMoved = true;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for (ChessPosition position : VALID_POSITIONS) { // Iterate across board to find King Position
            ChessPiece piece = board.getPiece(position);
            if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                kingPosition = position;
            } else {
                continue;
            }
        }

        return piecePositionInDanger(teamColor, kingPosition);
    }

    private boolean piecePositionInDanger(TeamColor teamColor, ChessPosition piecePosition) {

        for (ChessPosition position : VALID_POSITIONS) {
            ChessPiece piece = board.getPiece(position);
            if (piece == null || piece.getTeamColor() == teamColor) {
                continue;
            }
            Collection<ChessMove> moves = piece.pieceMoves(board, position);
            for (ChessMove move : moves) {
                if (move.getEndPosition().equals(piecePosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && hasNoValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
      return !isInCheck(teamColor) && hasNoValidMoves(teamColor);
    }

    private boolean hasNoValidMoves(TeamColor teamColor) {
        for (ChessPosition position : VALID_POSITIONS) {
            ChessPiece piece = board.getPiece(position);
            if (piece == null || piece.getTeamColor() != teamColor) {
                continue;
            }
            if (!validMoves(position).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
