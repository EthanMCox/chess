package dataaccess.inMemory;

import chess.ChessGame;
import dataaccess.GameDAO;
import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
  private int nextId = 1;
  final private HashMap<Integer, GameData> games = new HashMap<>();
  @Override
  public void clear() {
    games.clear();
  }

  @Override
  public int createGame(String gameName) {
    int currentId = nextId;
    games.put(currentId, new GameData(currentId, null, null, gameName, new ChessGame()));
    nextId++;
    return currentId;
  }

  @Override
  public GameData getGame(int gameID) {
    return games.get(gameID);
  }

  @Override
  public void updateGame(GameData gameData) {
    games.put(gameData.gameID(), gameData);
  }

  @Override
  public Collection<GameData> listGames() {
    return new ArrayList<>(games.values());
  }
}
