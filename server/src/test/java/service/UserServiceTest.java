package service;

import dataaccess.DataAccessException;
import dataaccess.memorydataaccess.AuthDataMemoryAccess;
import dataaccess.memorydataaccess.GameDataMemoryAccess;
import dataaccess.memorydataaccess.UserDataMemoryAccess;
import model.UserData;
import model.AuthData;
import model.GameData;
import model.GameSummaryData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
  static final UserDataMemoryAccess USER_DATA_ACCESS = new UserDataMemoryAccess();
  static final AuthDataMemoryAccess AUTH_DATA_ACCESS = new AuthDataMemoryAccess();
  static final GameDataMemoryAccess GAME_DATA_ACCESS = new GameDataMemoryAccess();
  static final UserService USER_SERVICE = new UserService(USER_DATA_ACCESS, AUTH_DATA_ACCESS);
  static final AuthService AUTH_SERVICE = new AuthService(AUTH_DATA_ACCESS);
  static final GameService GAME_SERVICE = new GameService(GAME_DATA_ACCESS, AUTH_DATA_ACCESS);

  @BeforeEach
  void clear() throws DataAccessException {
    USER_SERVICE.clearUserData();
  }

  @Test
  void clearGood() throws DataAccessException {
    // given
    new UserData("username", "password", "email");

    // when
    USER_SERVICE.clearUserData();

    // then
    try {
      USER_DATA_ACCESS.getUser("username");
    } catch (DataAccessException e) {
      assertEquals(e.getMessage(), "Error: No user found, please create a new user");
    }
  }

  @Test
  void clearBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = USER_SERVICE.register(userData);
    // when
    USER_SERVICE.clearUserData();
    AUTH_SERVICE.clearAuthData();

    // then
    try {
      AUTH_DATA_ACCESS.getAuthData(authData.authToken());
    } catch (DataAccessException e) {
      assertEquals(e.getMessage(), "Error: unauthorized");
    }
  }

  @Test
  void registerGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");

    // when
    AuthData authData = USER_SERVICE.register(userData);

    // then
    assertEquals(USER_DATA_ACCESS.getUser("username"), userData);
    assertEquals(authData, AUTH_DATA_ACCESS.getAuthData(authData.authToken()));
  }

  @Test
  void registerBad() throws DataAccessException {
    // given
    UserData userData = new UserData("", "password", "email");

    // when
    try {
      USER_SERVICE.register(userData);
    } catch (DataAccessException e) {
      // then
      assertEquals(e.getMessage(), "Error: bad reqeust");
    }
  }

  @Test
  void loginGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    UserData newUserData = new UserData(userData.username(), BCrypt.hashpw(userData.password(), BCrypt.gensalt()), userData.email());
    AuthData regAuthData = USER_SERVICE.register(newUserData);

    // when
    AuthData logAuthData = USER_SERVICE.login("username", "password");

    // then
    assertEquals(logAuthData.username(), "username");
    assertEquals(logAuthData, AUTH_DATA_ACCESS.getAuthData(logAuthData.authToken()));
    assertNotEquals(regAuthData, logAuthData);
  }

  @Test
  void loginBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    USER_SERVICE.register(userData);

    // when
    try {
      USER_SERVICE.login("userName", "password");
    } catch (DataAccessException e) {
      // then
      assertEquals(e.getMessage(), "Error: unauthorized");
    }
  }

  @Test
  void logoutGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    UserData newUserData = new UserData(userData.username(), BCrypt.hashpw(userData.password(), BCrypt.gensalt()), userData.email());
    USER_SERVICE.register(newUserData);
    AuthData loginAuthData = USER_SERVICE.login("username", "password");

    // when
    USER_SERVICE.logout(loginAuthData.authToken());

    // then
    try {
      AUTH_DATA_ACCESS.getAuthData(loginAuthData.authToken());
    } catch (DataAccessException e) {
      assertEquals("Error: unauthorized", e.getMessage());
    }

  }

  @Test
  void logoutBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData regAuthData = USER_SERVICE.register(userData);

    // when
    USER_SERVICE.logout(regAuthData.authToken());

    // then
    try {
      AUTH_DATA_ACCESS.getAuthData(regAuthData.authToken());
    } catch (DataAccessException e) {
      assertEquals("Error: unauthorized", e.getMessage());
    }
  }

  @Test
  void createGameGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = USER_SERVICE.register(userData);

    // when
    int gameId = GAME_SERVICE.createGame(authData.authToken(), "gameName");

    // then
    assertEquals(1, gameId);
  }

  @Test
  void creatGameBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    USER_SERVICE.register(userData);

    // when and then
    try {
      GAME_SERVICE.createGame("123", "gameName");
    } catch (DataAccessException e) {
      assertEquals("Error: unauthorized", e.getMessage());
    }
  }

  @Test
  void listGameGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = USER_SERVICE.register(userData);
    GAME_SERVICE.createGame(authData.authToken(), "gameName");

    // when
    Collection<GameSummaryData> games = GAME_SERVICE.listGames(authData.authToken());

    // then
    assertEquals(games.size(), 2);
  }

  @Test
  void listGameBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = USER_SERVICE.register(userData);
    GAME_SERVICE.createGame(authData.authToken(), "gameName");

    // when and then
    try {
      GAME_SERVICE.listGames("1234");
    } catch (DataAccessException e) {
      assertEquals("Error: unauthorized", e.getMessage());
    }
  }

  @Test
  void joinGameGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = USER_SERVICE.register(userData);
    int gameId = GAME_SERVICE.createGame(authData.authToken(), "gameName");

    // when
    GAME_SERVICE.joinGame(authData.authToken(), "WHITE", gameId);
    GameData gameData = GAME_SERVICE.getGame(authData.authToken(), gameId);

    // then
    assertEquals(gameData.whiteUsername(), userData.username());
  }

  @Test
  void joinGameBad() throws DataAccessException {
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = USER_SERVICE.register(userData);
    int gameId = GAME_SERVICE.createGame(authData.authToken(), "gameName");
    GAME_SERVICE.joinGame(authData.authToken(), "WHITE", gameId);
    GAME_SERVICE.getGame(authData.authToken(), gameId);
    UserData user1Data = new UserData("username1", "password", "email");
    AuthData auth1Data = USER_SERVICE.register(user1Data);

    // when and then
    try {
      GAME_SERVICE.joinGame(auth1Data.authToken(), "WHITE", gameId);
    } catch (DataAccessException e) {
      assertEquals("Error: already taken", e.getMessage());
    }
  }

}
