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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", teamTurn=" + teamTurn +
                '}';
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
        addEnPassantMoves(startPosition, possibleMoves, piece);
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

    private void addEnPassantMoves(ChessPosition startPosition, Collection<ChessMove> possibleMoves, ChessPiece piece) {
        if (piece == null || piece.getPieceType() != ChessPiece.PieceType.PAWN) {
            return;
        }
        ChessGame.TeamColor pieceColor = piece.getTeamColor();
        int row = startPosition.getRow();
        int direction;
        if (row == 5 && piece.getTeamColor() == TeamColor.WHITE) {
            direction = 1;
        }
        else if (row == 4 && piece.getTeamColor() == TeamColor.BLACK) {
            direction = -1;
        }
        else {
            return;
        }
        int col = startPosition.getColumn();
        ChessPiece pieceLeft = board.getPiece(new ChessPosition(row, col - 1));
        ChessPiece pieceRight = board.getPiece(new ChessPosition(row, col + 1));
        if ((pieceLeft != null && pieceLeft.getPieceType() == ChessPiece.PieceType.PAWN) && (pieceLeft.getTeamColor() != pieceColor) && enPassantIsValid(row, col, direction, col - 1)) {
            addEnPassantMove(startPosition, possibleMoves, row, direction, col - 1);
        }
        if ((pieceRight != null && pieceRight.getPieceType() == ChessPiece.PieceType.PAWN) && (pieceRight.getTeamColor() != pieceColor) && enPassantIsValid(row, col, direction, col + 1)) {
            addEnPassantMove(startPosition, possibleMoves, row, direction, col + 1);
        }
    }

    private boolean enPassantIsValid(int row, int column, int direction, int targetColumn) {
        ChessPosition diagonalPosition = new ChessPosition(row + direction, targetColumn);
        ChessPosition twoSpacesForward = new ChessPosition(row + 2 * direction, column);
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

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();

        if (startPosition == null || !VALID_POSITIONS.contains(startPosition)) {
            throw new InvalidMoveException("Invalid start position");
        } else if (board.getPiece(startPosition) == null){
            throw new InvalidMoveException("No piece at start position");
        } else if (!validMoves(startPosition).contains(move)) {
            throw new InvalidMoveException("Invalid move");
        } else if (!board.getPiece(startPosition).getTeamColor().equals(teamTurn)) {
            throw new InvalidMoveException("Not your turn");
        }
        else {
            previousBoardState = new ChessBoard(board);
            board.movePiece(move);
            teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
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

        // If any piece can capture the king, then the team is in check
        for (ChessPosition position : VALID_POSITIONS) {
            ChessPiece piece = board.getPiece(position);
            if (piece == null || piece.getTeamColor() == teamColor) {
                continue;
            }
            Collection<ChessMove> moves = piece.pieceMoves(board, position);
            for (ChessMove move : moves) {
                if (move.getEndPosition().equals(kingPosition)) {
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
