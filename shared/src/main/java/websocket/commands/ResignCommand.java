package websocket.commands;

import java.util.Objects;

public class ResignCommand extends UserGameCommand{
  private Integer gameID;
  public ResignCommand(String authToken, Integer gameID) {
    super(authToken);
    this.commandType = CommandType.RESIGN;
    this.gameID = gameID;
  }

  public Integer getGameID() {
    return gameID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ResignCommand that = (ResignCommand) o;
    return Objects.equals(gameID, that.gameID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), gameID);
  }
}
