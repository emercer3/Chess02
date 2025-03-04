package dataaccess.memoryDataAccess;

import model.GameData;
import model.UserData;
import model.GameSummaryData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;

public class GameDataMemoryAccess implements GameDataAccess {
  final private HashMap<Integer, GameData> games = new HashMap<>();
  private int gameID = 1;

  @Override
  public Collection<GameSummaryData> listGames() throws DataAccessException {
    Collection<GameSummaryData> gameList = new ArrayList<>();
    for (GameData game : games.values()) {
      gameList.add(new GameSummaryData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
    }
    // Collection<GameData> gameList = games.values();
    return gameList;
  }

  @Override
  public int createGame(String gameName) throws DataAccessException {
    GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
    games.put(gameID, gameData);
    gameID++;
    return gameData.gameID();
  }

  @Override
  public GameData getGame(int gameId) throws DataAccessException {
    return games.get(gameId);
  }

  @Override
  public void updateGame(GameData gameData) throws DataAccessException {
    games.remove(gameData.gameID());
    games.put(gameData.gameID(), gameData);
  }

  @Override
  public void clearGameData() throws DataAccessException {
    games.clear();
  }
  
}
