package Service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import model.AuthData;
import model.GameData;
import model.GameSummaryData;
import chess.ChessGame;

import java.util.Collection;

public class GameService {
  private final GameDataAccess gameDataAccess;
  private final AuthDataAccess authDataAccess;

  public GameService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
    this.gameDataAccess = gameDataAccess;
    this.authDataAccess = authDataAccess;
  }

  public void clearGameData() throws DataAccessException {
    try {
      gameDataAccess.clearGameData();
    } catch (DataAccessException e) {}
  }

  public GameData getGame(String authToken, int gameId) throws DataAccessException {
    try {
      AuthData authData = authDataAccess.geAuthData(authToken);
      return gameDataAccess.getGame(gameId);
    } catch (DataAccessException e) {
      throw new DataAccessException(e.getMessage());
    }
  }

  
  public Collection<GameSummaryData> listGames(String authToken) throws DataAccessException {
    
    try {
      AuthData authData = authDataAccess.geAuthData(authToken);  // do i need to get authData
      return gameDataAccess.listGames();
    } catch (DataAccessException e) {
      throw new DataAccessException(e.getMessage());
    }
  }


  public int createGame(String authToken, String gameName) throws DataAccessException {
    try {
      AuthData authData = authDataAccess.geAuthData(authToken);
      return gameDataAccess.createGame(gameName);
    } catch (DataAccessException e) {
      throw new DataAccessException(e.getMessage());
    }
  }


  public void joinGame(String authToken, String playerColor, int gameId) throws DataAccessException {
    try {
      AuthData authData = authDataAccess.geAuthData(authToken);
      GameData gameData = gameDataAccess.getGame(gameId);
      GameData newGameData;
      if (playerColor.equals("WHITE")) {
        newGameData = new GameData(gameId, authData.username(), gameData.blackUsername(), gameData.gameName(), gameData.game());
      } else {
        newGameData = new GameData(gameId, gameData.whiteUsername(), authData.username(), gameData.gameName(), gameData.game());
      }
      gameDataAccess.updateGame(newGameData);
    } catch (DataAccessException e) {
      throw new DataAccessException(e.getMessage());
    }
  }
}
