package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import model.AuthData;
import model.GameData;
import model.GameSummaryData;


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
    } catch (DataAccessException e) {
    }
  }

  public GameData getGame(String authToken, int gameId) throws DataAccessException {
    try {
      authDataAccess.getAuthData(authToken);
      return gameDataAccess.getGame(gameId);
    } catch (DataAccessException e) {
      throw new DataAccessException(e.getMessage());
    }
  }

  public Collection<GameSummaryData> listGames(String authToken) throws DataAccessException {

    try {
      AuthData authData = authDataAccess.getAuthData(authToken);
      if (authData == null) {
        throw new DataAccessException("Error: unauthorized");
      }
      return gameDataAccess.listGames();
    } catch (DataAccessException e) {
      throw new DataAccessException(e.getMessage());
    }
  }

  public int createGame(String authToken, String gameName) throws DataAccessException {
    AuthData auth = null;
    try {
      auth = authDataAccess.getAuthData(authToken);
      if (auth == null) {
        throw new DataAccessException("Error: unauthorized");
      }
      return gameDataAccess.createGame(gameName);
    } catch (DataAccessException e) {
      throw new DataAccessException(e.getMessage());
    }
  }

  public void joinGame(String authToken, String playerColor, int gameId) throws DataAccessException {
    if (playerColor == null || (!playerColor.equals("WHITE") && !playerColor.equals("BLACK"))) {
      throw new DataAccessException("Error: bad request");
    }

    AuthData authData;
    GameData gameData;

    try {
      authData = authDataAccess.getAuthData(authToken);
      if (authData == null) {
        throw new DataAccessException("Error: unauthorized");
      }
      gameData = gameDataAccess.getGame(gameId);
    } catch (DataAccessException e) {
      throw new DataAccessException(e.getMessage());
    }

    if (gameData == null) {
      throw new DataAccessException("Error: bad request");
    }

    GameData newGameData;
    if (playerColor.equals("WHITE") && gameData.whiteUsername() == null) {
      newGameData = new GameData(gameId, authData.username(), gameData.blackUsername(), gameData.gameName(),
          gameData.game());
    } else if (playerColor.equals("BLACK") && gameData.blackUsername() == null) {
      newGameData = new GameData(gameId, gameData.whiteUsername(), authData.username(), gameData.gameName(),
          gameData.game());
    } else {
      throw new DataAccessException("Error: already taken");
    }

    try {
      gameDataAccess.updateGame(newGameData);
    } catch (DataAccessException e) {
      throw new DataAccessException(e.getMessage());
    }
  }
}
