package chess;

import exception.ExceptionResult;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

  private final int row;
  private final int col;

  public ChessPosition(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public ChessPosition(String position) throws ExceptionResult {
    if (position.length() != 2) {
      throw new ExceptionResult(400, "Invalid position");
    }
    String col = position.substring(0, 1).toLowerCase();
    if (col.charAt(0) < 'a' || col.charAt(0) > 'h') {
      throw new ExceptionResult(400, "Invalid position");
    }
    if (!Character.isDigit(position.charAt(1))) {
      throw new ExceptionResult(400, "Invalid position");
    }
    this.row = Integer.parseInt(position.substring(1));
    this.col = col.charAt(0) - 'a' + 1;
  }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ChessPosition that = (ChessPosition) o;
    return row == that.row && col == that.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }

  @Override
  public String toString() {
    return "ChessPosition{" +
            "row=" + row +
            ", col=" + col +
            '}';
  }
}
