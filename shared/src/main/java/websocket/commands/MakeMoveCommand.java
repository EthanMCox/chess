package websocket.commands;

import chess.ChessMove;

import java.util.Objects;

public class MakeMoveCommand extends UserGameCommand {
  private ChessMove move;
  public MakeMoveCommand(String authToken, Integer gameID, ChessMove move) {
    super(authToken);
    this.commandType = CommandType.MAKE_MOVE;
    super.setGameID(gameID);
    this.move = move;
  }

  public ChessMove getMove() {
    return move;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    MakeMoveCommand that = (MakeMoveCommand) o;
    return Objects.equals(move, that.move);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), move);
  }
}
