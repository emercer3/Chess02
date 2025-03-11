package dataaccess.sqldataaccess;

import model.GameData;
import model.GameSummaryData;

import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;

public class GameSQLDataAccess implements dataaccess.GameDataAccess {

  @Override
  public Collection<GameSummaryData> listGames() throws DataAccessException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'listGames'");
  }

  @Override
  public int createGame(String gameName) throws DataAccessException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createGame'");
  }

  @Override
  public GameData getGame(int gameId) throws DataAccessException {
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
    // var statement = "TRUNCATE gameData";
    // executeUpdate(statement);
    throw new UnsupportedOperationException("Unimplemented method 'createGame'");
  }
  
}
