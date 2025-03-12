package dataaccess.sqldataaccess;

// import model.AuthData;
import model.GameData;
import model.GameSummaryData;
import chess.ChessGame;
import dataaccess.DataAccessException;
// import dataaccess.GameDataAccess;

import java.util.Collection;
// import java.util.HashMap;
import java.util.ArrayList;
import com.google.gson.Gson;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class GameSQLDataAccess implements dataaccess.GameDataAccess {

  private final String[] createStatements = {
    """
    CREATE TABLE IF NOT EXISTS gameData (
      `gameID` int NOT NULL AUTO_INCREMENT,
      `WhiteUsername` varchar(255) NOT NULL,
      `BlackUsername` varchar(255) NOT NULL,
      `gameName` varchar(255) NOT NULL,
      `game` TEXT NOT NULL,
      PRIMARY KEY (`gameID`),
      INDEX(`gameID`)
    )
    """
  };

  private void configureDatabase() throws DataAccessException{
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(e.getMessage());
    }

  }

  private int executeUpdate(String statement, Object... params) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try ( var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
        for (var i = 0; i < params.length; i++) {
          var param = params[i];
          if (param instanceof String p) ps.setString(i + 1, p);
          else if (param instanceof Integer p) ps.setInt(i + 1, p);
          else if (param == null) ps.setNull(i + 1, NULL);
        }
        ps.executeUpdate();

        var rs = ps.getGeneratedKeys();
        if (rs.next()) {
          return rs.getInt(1);
        }

        return 0;
      }
    } catch (Exception e) {
      throw new DataAccessException(e.getMessage());
    }
  }

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
    configureDatabase();
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
            gameList.add(new GameSummaryData(gameId,whiteUsername, blackUsername, gameName));
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
    var statement = "INSERT INTO gameData (gameID, WhiteUsername, BlackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
    return executeUpdate(statement, null, null, gameName, new Gson().toJson(new ChessGame()));
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
    executeUpdate(statement, gameData.whiteUsername(), gameData.blackUsername(), new Gson().toJson(gameData.game()), gameData.gameID());
  }

  @Override
  public void clearGameData() throws DataAccessException {
    var statement = "TRUNCATE gameData";
    executeUpdate(statement);
  }
  
}
