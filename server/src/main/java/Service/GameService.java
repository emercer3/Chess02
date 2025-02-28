package Service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import model.AuthData;
import model.GameData;

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
}
