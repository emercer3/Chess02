package dataaccess.memoryDataAccess;

import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;

public class GameDataMemoryAccess implements GameDataAccess {
  final private HashMap<Integer, GameData> games = new HashMap<>();
  private int gameID = 1;

  @Override
  public Collection<GameData> listGames() throws DataAccessException {
    Collection<GameData> gameList = games.values();
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
  public GameData getGame(String gameName) throws DataAccessException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getGame'");
  }

  @Override
  public void updateGame(GameData gameData) throws DataAccessException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateGame'");
  }

  @Override
  public void clearGameData() throws DataAccessException {
    games.clear();
  }
  
}
