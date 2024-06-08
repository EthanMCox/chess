package chesspositiontests;

import exception.ExceptionResult;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import chess.*;

public class ChessPositionTest {

  @Test
  @DisplayName("a1")
  public void a1() throws ExceptionResult {
    var position = new ChessPosition("a1");
    assertEquals(1, position.getRow());
    assertEquals(1, position.getColumn());
  }
  @Test
  @DisplayName("h8")
  public void h8() throws ExceptionResult {
    var position = new ChessPosition("h8");
    assertEquals(8, position.getRow());
    assertEquals(8, position.getColumn());
  }

  @Test
  @DisplayName("col out of bounds")
  public void colOutOfBounds() {
    assertThrows(ExceptionResult.class, () -> new ChessPosition("i1"));
  }

  @Test
  @DisplayName("F3")
  public void f3() throws ExceptionResult {
    var position = new ChessPosition("F3");
    assertEquals(3, position.getRow());
    assertEquals(6, position.getColumn());
  }
}
