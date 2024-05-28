package dataaccess.mysql;

import dataaccess.GameDAO;
import exception.ExceptionResult;
import model.GameData;
import model.ListGamesData;

import java.util.Collection;

public class MySQLGameDAO implements GameDAO {
  @Override
  public void clear() throws ExceptionResult {

  }

  @Override
  public int createGame(String gameName) throws ExceptionResult {
    return 0;
  }

  @Override
  public GameData getGame(int gameId) throws ExceptionResult {
    return null;
  }

  @Override
  public Collection<ListGamesData> listGames() throws ExceptionResult {
    return null;
  }

  @Override
  public void updateGame(GameData gameData) throws ExceptionResult {

  }
}
