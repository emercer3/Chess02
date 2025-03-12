package dataaccess.sqldataaccess;

import model.GameData;
import model.GameSummaryData;
import chess.ChessGame;
import dataaccess.DataAccessException;

import java.util.Collection;
import java.util.ArrayList;
import com.google.gson.Gson;

import java.sql.*;

public class GameSQLDataAccess implements dataaccess.GameDataAccess {

  private GameData readGameData(ResultSet rs) throws SQLException {
    var gameId = rs.getInt("gameID");
    var whiteUsername = rs.getString("WhiteUsername");
    var blackUsername = rs.getString("BlackUsername");
    var gameName = rs.getString("gameName");
    var json = rs.getString("game");
    var game = new Gson().fromJson(json, ChessGame.class);
    GameData gameData = new GameData(gameId, whiteUsername, blackUsername, gameName, game);
    return gameData;
  }

  public GameSQLDataAccess() throws DataAccessException {
    DataBase.configureDatabase();
  }

  @Override
  public Collection<GameSummaryData> listGames() throws DataAccessException {
    Collection<GameSummaryData> gameList = new ArrayList<>();
    var statement = "SELECT gameID, WhiteUsername, BlackUsername, gameName FROM gameData";
    try (var conn = DatabaseManager.getConnection()) {
      try (var ps = conn.prepareStatement(statement)) {
        try (var rs = ps.executeQuery()) {
          while (rs.next()) {
            var gameId = rs.getInt("gameID");
            var whiteUsername = rs.getString("WhiteUsername");
            var blackUsername = rs.getString("BlackUsername");
            var gameName = rs.getString("gameName");
            gameList.add(new GameSummaryData(gameId, whiteUsername, blackUsername, gameName));
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(e.getMessage());
    }
    return gameList;
  }

  @Override
  public int createGame(String gameName) throws DataAccessException {
    var statement = "INSERT INTO gameData (gameName, game) VALUES (?, ?)";
    return DataBase.executeUpdate(statement, gameName, new Gson().toJson(new ChessGame()));
  }

  @Override
  public GameData getGame(int gameId) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT gameID, WhiteUsername, BlackUsername, gameName, game FROM gameData WHERE gameID=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setInt(1, gameId);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            return readGameData(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(e.getMessage());
    }
    return null;
  }

  @Override
  public void updateGame(GameData gameData) throws DataAccessException {
    var statement = "UPDATE gameData SET WhiteUsername=?, BlackUsername=?, game=? WHERE gameID=?";
    DataBase.executeUpdate(statement, gameData.whiteUsername(), gameData.blackUsername(), new Gson().toJson(gameData.game()),
        gameData.gameID());
  }

  @Override
  public void clearGameData() throws DataAccessException {
    var statement = "TRUNCATE gameData";
    DataBase.executeUpdate(statement);
  }

}
