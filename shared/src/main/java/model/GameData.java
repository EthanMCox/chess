package model;

import chess.ChessGame;

record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
  public GameData updateGameID(int gameID) {
    return new GameData(gameID, this.whiteUsername, this.blackUsername, this.gameName, this.game);
  }
  public GameData updateWhiteUsername(String whiteUsername) {
    return new GameData(this.gameID, whiteUsername, this.blackUsername, this.gameName, this.game);
  }
  public GameData updateBlackUsername(String blackUsername) {
    return new GameData(this.gameID, this.whiteUsername, blackUsername, this.gameName, this.game);
  }
  public GameData updateGameName(String gameName) {
    return new GameData(this.gameID, this.whiteUsername, this.blackUsername, gameName, this.game);
  }
  public GameData updateGame(ChessGame game) {
    return new GameData(this.gameID, this.whiteUsername, this.blackUsername, this.gameName, game);
  }
}
