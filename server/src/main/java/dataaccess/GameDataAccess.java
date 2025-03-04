package dataaccess;

import dataaccess.DataAccessException;
import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDataAccess {
  Collection<GameData> listGames() throws DataAccessException;

  String createGame(String authToken) throws DataAccessException;
  
  GameData getGame(String gameName) throws DataAccessException;

  void updateGame(GameData gameData) throws DataAccessException;

  void clearGameData() throws DataAccessException;
}