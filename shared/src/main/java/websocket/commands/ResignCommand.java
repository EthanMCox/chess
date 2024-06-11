package websocket.commands;

import java.util.Objects;

public class ResignCommand extends UserGameCommand{
  public ResignCommand(String authToken, Integer gameID) {
    super(authToken);
    this.commandType = CommandType.RESIGN;
    super.setGameID(gameID);
  }
}
