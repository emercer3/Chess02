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

}
