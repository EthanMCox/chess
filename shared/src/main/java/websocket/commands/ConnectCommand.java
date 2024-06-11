package websocket.commands;

import java.util.Objects;

public class ConnectCommand extends UserGameCommand {
  public ConnectCommand(String authToken, Integer gameID) {
    super(authToken);
    this.commandType = CommandType.CONNECT;
    super.setGameID(gameID);
  }
}
