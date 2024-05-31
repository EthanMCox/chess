package dataaccess.mysql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import exception.ExceptionResult;
import model.GameData;
import model.ListGamesData;
import util.JsonSerializer;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class MySQLGameDAO extends SQLUpdateExecutor implements GameDAO {
  @Override
  public void clear() throws ExceptionResult {
    var statement = "TRUNCATE TABLE game";
    executeUpdate(statement);
  }

  @Override
  public int createGame(String gameName) throws ExceptionResult {
    var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
    String game = JsonSerializer.serialize((new ChessGame()));
    return executeUpdate(statement, null, null, gameName, game);
  }

  @Override
  public GameData getGame(int gameId) throws ExceptionResult {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT * FROM game WHERE gameId=?";
      try (var stmt = conn.prepareStatement(statement)) {
        stmt.setInt(1, gameId);
        try (var rs = stmt.executeQuery()) {
          if (rs.next()) {
            ChessGame game = JsonSerializer.deserialize(rs.getString("game"), ChessGame.class);
            return new GameData(rs.getInt("gameId"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), game);
          } else {
            return null;
          }
        }
      }
    } catch (SQLException | DataAccessException e) {
      throw new ExceptionResult(500, String.format("unable to read database: %s", e.getMessage()));
    }
  }

  @Override
  public Collection<ListGamesData> listGames() throws ExceptionResult {
    Collection<ListGamesData> listGamesData = new HashSet<>();
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT gameId, whiteUsername, blackUsername, gameName FROM game";
      try (var stmt = conn.prepareStatement(statement)) {
        try (var rs = stmt.executeQuery()) {
          while (rs.next()) {
            listGamesData.add(new ListGamesData(rs.getInt("gameId"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName")));
          }
        }
      }
      return listGamesData;
    } catch (SQLException | DataAccessException e) {
      throw new ExceptionResult(500, String.format("unable to read database: %s", e.getMessage()));
    }
  }

  @Override
  public void updateGame(GameData gameData) throws ExceptionResult {

  }
}
