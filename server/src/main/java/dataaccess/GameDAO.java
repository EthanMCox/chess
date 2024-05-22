package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
  void clear();
  void createGame(String gameName);
  GameData getGame(String gameId);
  Collection<GameData> listGames();
  void updateGame(GameData gameData);
}
