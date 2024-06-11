package websocket.commands;

import java.util.Objects;

public class LeaveCommand extends UserGameCommand {
  public LeaveCommand(String authToken, Integer gameID) {
    super(authToken);
    this.commandType = CommandType.LEAVE;
    super.setGameID(gameID);
  }
}
