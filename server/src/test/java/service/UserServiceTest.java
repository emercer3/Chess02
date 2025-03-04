package service;

import dataaccess.DataAccessException;
import dataaccess.memoryDataAccess.UserDataMemoryAccess;
import dataaccess.memoryDataAccess.AuthDataMemoryAccess;
import dataaccess.memoryDataAccess.GameDataMemoryAccess;
import model.UserData;
import model.AuthData;
import model.GameData;
import model.GameSummaryData;
import Service.AuthService;
import Service.GameService;
import Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
  static final UserDataMemoryAccess UserDataAccess = new UserDataMemoryAccess();
  static final AuthDataMemoryAccess AuthDataAccess = new AuthDataMemoryAccess();
  static final GameDataMemoryAccess GameDataAccess = new GameDataMemoryAccess();
  static final UserService UserService = new UserService(UserDataAccess, AuthDataAccess);
  static final AuthService AuthService = new AuthService(AuthDataAccess);
  static final GameService GameService = new GameService(GameDataAccess, AuthDataAccess);

  @BeforeEach
  void clear() throws DataAccessException {
    UserService.clearUserData();
  }

  @Test
  void clearGood() throws DataAccessException {
    // given
    new UserData("username", "password", "email");

    // when
    UserService.clearUserData();

    // then
    try {
      UserDataAccess.getUser("username");
    } catch (DataAccessException e) {
      assertEquals(e.getMessage(), "Error: No user found, please create a new user");
    }
  }

  @Test
  void clearBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = UserService.register(userData);
    // when
    UserService.clearUserData();
    AuthService.clearAuthData();

    // then
    try {
      AuthDataAccess.getAuthData(authData.authToken());
    } catch (DataAccessException e) {
      assertEquals(e.getMessage(), "Error: unauthorized");
    }
  }

  @Test
  void registerGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");

    // when
    AuthData authData = UserService.register(userData);

    // then
    assertEquals(UserDataAccess.getUser("username"), userData);
    assertEquals(authData, AuthDataAccess.getAuthData(authData.authToken()));
  }

  @Test
  void registerBad() throws DataAccessException {
    // given
    UserData userData = new UserData("", "password", "email");

    // when
    try {
      UserService.register(userData);
    } catch (DataAccessException e) {
      // then
      assertEquals(e.getMessage(), "Error: bad reqeust");
    }
  }

  @Test
  void loginGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData regAuthData = UserService.register(userData);

    // when
    AuthData logAuthData = UserService.login("username", "password");

    // then
    assertEquals(logAuthData.username(), "username");
    assertEquals(logAuthData, AuthDataAccess.getAuthData(logAuthData.authToken()));
    assertNotEquals(regAuthData, logAuthData);
  }

  @Test
  void loginBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    UserService.register(userData);

    // when
    try {
      UserService.login("userName", "password");
    } catch (DataAccessException e) {
      // then
      assertEquals(e.getMessage(), "Error: unauthorized");
    }
  }

  @Test
  void logoutGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    UserService.register(userData);
    AuthData loginAuthData = UserService.login("username", "password");

    // when
    UserService.logout(loginAuthData.authToken());

    // then
    try {
      AuthDataAccess.getAuthData(loginAuthData.authToken());
    } catch (DataAccessException e) {
      assertEquals("no autherization found", e.getMessage());
    }

  }

  @Test
  void logoutBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData regAuthData = UserService.register(userData);

    // when
    UserService.logout(regAuthData.authToken());

    // then
    try {
      AuthDataAccess.getAuthData(regAuthData.authToken());
    } catch (DataAccessException e) {
      assertEquals("no autherization found", e.getMessage());
    }
  }

  @Test
  void createGameGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = UserService.register(userData);

    // when
    int gameId = GameService.createGame(authData.authToken(), "gameName");

    // then
    assertEquals(1, gameId);
  }

  @Test
  void creatGameBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    UserService.register(userData);

    // when and then
    try {
      GameService.createGame("123", "gameName");
    } catch (DataAccessException e) {
      assertEquals("Error: unauthorized", e.getMessage());
    }
  }

  @Test
  void listGameGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = UserService.register(userData);
    GameService.createGame(authData.authToken(), "gameName");

    // when
    Collection<GameSummaryData> games = GameService.listGames(authData.authToken());

    // then
    assertEquals(games.size(), 2);
  }

  @Test
  void listGameBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = UserService.register(userData);
    GameService.createGame(authData.authToken(), "gameName");

    // when and then
    try {
      GameService.listGames("1234");
    } catch (DataAccessException e) {
      assertEquals("Error: unauthorized", e.getMessage());
    }
  }

  @Test
  void joinGameGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = UserService.register(userData);
    int gameId = GameService.createGame(authData.authToken(), "gameName");

    // when
    GameService.joinGame(authData.authToken(), "WHITE", gameId);
    GameData gameData = GameService.getGame(authData.authToken(), gameId);

    // then
    assertEquals(gameData.whiteUsername(), userData.username());
  }

  @Test
  void joinGameBad() throws DataAccessException {
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = UserService.register(userData);
    int gameId = GameService.createGame(authData.authToken(), "gameName");
    GameService.joinGame(authData.authToken(), "WHITE", gameId);
    GameService.getGame(authData.authToken(), gameId);
    UserData user1Data = new UserData("username1", "password", "email");
    AuthData auth1Data = UserService.register(user1Data);

    // when and then
    try {
      GameService.joinGame(auth1Data.authToken(), "WHITE", gameId);
    } catch (DataAccessException e) {
      assertEquals("Error: already taken", e.getMessage());
    }
  }

}
