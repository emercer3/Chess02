package dataaccess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chess.ChessGame.TeamColor;
import dataaccess.sqldataaccess.*;
import model.*;

public class DAOTests {
  private final dataaccess.UserDataAccess userSQL;
  private final dataaccess.AuthDataAccess authSQL;
  private final dataaccess.GameDataAccess gameSQL;

  public DAOTests() {
    try {
      this.userSQL = new UserSQLDataAccess();
      this.authSQL = new AuthSQLDataAccess();
      this.gameSQL = new GameSQLDataAccess();
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

  @BeforeEach
  void clear() throws DataAccessException {
    userSQL.clearUserData();
    gameSQL.clearGameData();
    authSQL.clearAuthData();
  }

  @Test
  void createUserGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");

    // when
    userSQL.createUser(userData);
    UserData existingUserData = userSQL.getUser(userData.username());

    // then
    assertEquals(userData, existingUserData);

  }

  @Test
  void createUserBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    UserData newUserData = new UserData("username", "password", "email");
    userSQL.createUser(userData);

    // when
    try {
      userSQL.createUser(newUserData);
    } catch (Exception e) {

      // then
      assertEquals(e.getMessage(), "Duplicate entry 'username' for key 'userData.PRIMARY'");
    }
  }

  @Test
  void getUserGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    userSQL.createUser(userData);

    // when
    UserData existingUserData = userSQL.getUser(userData.username());

    // then
    assertEquals(userData, existingUserData);

  }

  @Test
  void getUserBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    userSQL.createUser(userData);
    UserData oldUserData = null;

    // when
    userSQL.clearUserData();
    try {
      oldUserData = userSQL.getUser(userData.username());
    } catch (Exception e) {
    }
    assertEquals(oldUserData, null);

  }

  @Test
  void deleteUserDataGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    userSQL.createUser(userData);

    // when
    userSQL.deleteUserData(userData.username());

    // then
    UserData existingUserData = userSQL.getUser(userData.username());
    assertEquals(existingUserData, null);

  }

  @Test
  void deleteUserDataBad() throws DataAccessException {
    // given

    // when
    try {
      userSQL.deleteUserData("Username");
    } catch (Exception e) {
      // then
      assertEquals(e.getMessage(), "error");
    }

  }

  @Test
  void clearUserDataGood() throws DataAccessException {
    // given
    userSQL.createUser(new UserData("username", "password", "email"));

    // when
    userSQL.clearUserData();

    // then
    assertEquals(userSQL.getUser("username"), null);
  }

  // auth Test cases
  @Test
  void createAuthGood() throws DataAccessException {
    // given
    String userName = "username";

    // when
    AuthData authData = authSQL.createAuth(userName);

    // then
    assertEquals(authData.username(), userName);

  }

  @Test
  void createAuthBad() throws DataAccessException {
    // given
    String userName = "username";
    new AuthData("1234", userName);

    // when and then
    assertThrows(DataAccessException.class, () -> authSQL.createAuth(null));
  }

  @Test
  void getAuthGood() throws DataAccessException {
    // given
    AuthData authData = authSQL.createAuth("username");

    // when
    AuthData authFromDB = authSQL.getAuthData(authData.authToken());

    // then
    assertEquals(authData, authFromDB);
  }

  @Test
  void getAuthBad() throws DataAccessException {
    // given
    authSQL.createAuth("username");

    // when
    AuthData badRequest = authSQL.getAuthData(null);

    // then
    assertEquals(badRequest, null);
  }

  @Test
  void deleteAuthGood() throws DataAccessException {
    // given
    AuthData oldAuthData = authSQL.createAuth("username");

    // when
    authSQL.deleteAuth(oldAuthData.authToken());

    // then
    AuthData currentAuthData = authSQL.getAuthData(oldAuthData.authToken());
    assertNotEquals(oldAuthData, currentAuthData);
  }

  @Test
  void deleteAuthBad() throws DataAccessException {
    // given
    AuthData oldAuthData = authSQL.createAuth("username");

    // when
    authSQL.deleteAuth("1234");

    // then
    assertEquals(authSQL.getAuthData(oldAuthData.authToken()), oldAuthData);
  }

  @Test
  void clearAuthDataGood() throws DataAccessException {
    // given
    AuthData authOne = authSQL.createAuth("username");
    AuthData authTwo = authSQL.createAuth("username2");

    // when
    authSQL.clearAuthData();

    // then
    assertEquals(authSQL.getAuthData(authOne.authToken()), null);
    assertEquals(authSQL.getAuthData(authTwo.authToken()), null);

  }

  // game SQL database Tests

  @Test
  void createGameGood() throws DataAccessException {
    // given
    String gameName = "game";

    // when
    int gameId = gameSQL.createGame(gameName);

    // then
    assertEquals(gameId, 1);
  }

  @Test
  void createGameBad() throws DataAccessException {
    // given
    String gameName = null;

    // when and then
    assertThrows(DataAccessException.class, () -> gameSQL.createGame(gameName));
  }

  @Test
  void getGameGood() throws DataAccessException {
    // given
    int gameId = gameSQL.createGame("game");

    // when
    GameData gameData = gameSQL.getGame(gameId);

    // then
    assertEquals(gameData.gameID(), gameId);
    assertEquals(gameData.gameName(), "game");
  }

  @Test
  void getGameBad() throws DataAccessException {
    // given nothing

    // when and then
    assertEquals(gameSQL.getGame(1), null);
  }

  @Test
  void updateGameGood() throws DataAccessException {
    // given
    int gameId = gameSQL.createGame("game");
    GameData gameData = gameSQL.getGame(gameId);
    gameData.game().setTeamTurn(TeamColor.BLACK);

    // when
    gameSQL.updateGame(gameData);

    // then
    assertEquals(gameSQL.getGame(gameId).game().getTeamTurn(), TeamColor.BLACK);

  }

  @Test
  void updateGameBad() throws DataAccessException {
    // given
    int gameId = gameSQL.createGame("game");
    GameData gameData = gameSQL.getGame(gameId);
    String longString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
        + "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
        + "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris "
        + "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in "
        + "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla "
        + "pariatur. Excepteur sint occaecat cupidatat non proident, sunt in "
        + "culpa qui officia deserunt mollit anim id est laborum.";
    GameData newGameData = new GameData(gameId, gameData.whiteUsername(), longString, "Game", null);

    // when
    assertThrows(DataAccessException.class, () -> gameSQL.updateGame(newGameData));
  }

  @Test
  void listGamesGood() throws DataAccessException {
    // given
    gameSQL.createGame("game");
    gameSQL.createGame("game2");

    // when
    Collection<GameSummaryData> gameList = gameSQL.listGames();

    // then
    assertEquals(gameList.size(), 2);
  }

  @Test
  void listGameBad() throws DataAccessException {
    // given nothing

    // when
    Collection<GameSummaryData> gameList = gameSQL.listGames();

    // then
    assertEquals(gameList.size(), 0);
  }

  @Test
  void clearGameDataGood() throws DataAccessException {
    // given
    gameSQL.createGame("game");
    gameSQL.createGame("game2");

    // when
    gameSQL.clearGameData();

    // then
    Collection<GameSummaryData> gameList = gameSQL.listGames();
    assertEquals(gameList.size(), 0);

  }

}