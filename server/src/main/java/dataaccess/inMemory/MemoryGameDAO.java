package dataaccess.inMemory;

import dataaccess.GameDAO;
import model.*;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
  @Override
  public void clear() {
  }

  @Override
  public void createGame(String gameID) {
  }

  @Override
  public GameData getGame(String gameID) {
    return null;
  }

  @Override
  public void updateGame(GameData gameData) {
  }

  @Override
  public Collection<GameData> listGames() {
    return null;
  }
}
