package websocket.commands;

import chess.ChessMove;

import java.util.Objects;

public class MakeMoveCommand extends UserGameCommand {
  private ChessMove move;
  private Integer gameID;
  public MakeMoveCommand(String authToken, Integer gameID, ChessMove move) {
    super(authToken);
    this.commandType = CommandType.MAKE_MOVE;
    this.gameID = gameID;
    this.move = move;
  }

  public ChessMove getMove() {
    return move;
  }

  public Integer getGameID() {
    return gameID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    MakeMoveCommand that = (MakeMoveCommand) o;
    return Objects.equals(move, that.move) && Objects.equals(gameID, that.gameID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), move, gameID);
  }
}
