package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        // -1?
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    public void removePiece(ChessPosition position) {
        squares[position.getRow()-1][position.getColumn()-1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        // -1 on both of these?
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i=0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                ChessPosition position = new ChessPosition(i+1, j+1);
                removePiece(position);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.equals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(squares);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                ChessPiece piece = squares[i][j];
                if (piece == null) {
                    sb.append("empty ");
                } else {
                    sb.append(piece.toString()).append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
