package dataaccess;

import dataaccess.DataAccessException;
import chess.ChessGame;
import model.GameData;
import model.GameSummaryData;

import java.util.Collection;

public interface GameDataAccess {
  Collection<GameSummaryData> listGames() throws DataAccessException;

  int createGame(String gameName) throws DataAccessException;
  
  GameData getGame(int gameId) throws DataAccessException;

  void updateGame(GameData gameData) throws DataAccessException;

  void clearGameData() throws DataAccessException;
}