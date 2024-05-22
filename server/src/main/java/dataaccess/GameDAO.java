package dataaccess;

import exception.ExceptionResult;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
  void clear() throws ExceptionResult;
  void createGame(String gameName) throws ExceptionResult;
  GameData getGame(String gameId) throws ExceptionResult;
  Collection<GameData> listGames() throws ExceptionResult;
  void updateGame(GameData gameData) throws ExceptionResult;
}
