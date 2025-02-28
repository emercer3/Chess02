package dataaccess.memoryDataAccess;

import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;

public class GameDataMemoryAccess implements GameDataAccess {
  final private HashMap<String, GameData> games = new HashMap<>();

  @Override
  public Collection<ChessGame> listGames(String authToken) throws DataAccessException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'listGames'");
  }

  @Override
  public String createGame(String authToken) throws DataAccessException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createGame'");
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
