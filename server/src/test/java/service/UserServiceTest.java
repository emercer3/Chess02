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
  static final UserDataMemoryAccess userDataAccess = new UserDataMemoryAccess();
  static final AuthDataMemoryAccess authDataAccess = new AuthDataMemoryAccess();
  static final GameDataMemoryAccess gameDataAccess = new GameDataMemoryAccess();
  static final UserService userService = new UserService(userDataAccess, authDataAccess);
  static final AuthService authService = new AuthService(authDataAccess);
  static final GameService gameService = new GameService(gameDataAccess, authDataAccess);

  @BeforeEach
  void clear() throws DataAccessException {
    userService.clearUserData();
  }

  @Test
  void clearGood() throws DataAccessException {
    // given
    new UserData("username", "password", "email");

    // when
    userService.clearUserData();

    // then
    try {
      userDataAccess.getUser("username");
    } catch (DataAccessException e) {
      assertEquals(e.getMessage(), "Error: No user found, please create a new user");
    }
  }

  @Test
  void clearBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = userService.register(userData);
    // when
    userService.clearUserData();
    authService.clearAuthData();

    // then
    try {
      authDataAccess.getAuthData(authData.authToken());
    } catch (DataAccessException e) {
      assertEquals(e.getMessage(), "Error: unauthorized");
    }
  }

  @Test
  void registerGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");

    // when
    AuthData authData = userService.register(userData);

    // then
    assertEquals(userDataAccess.getUser("username"), userData);
    assertEquals(authData, authDataAccess.getAuthData(authData.authToken()));
  }

  @Test
  void registerBad() throws DataAccessException {
    // given
    UserData userData = new UserData("", "password", "email");

    // when
    try {
      userService.register(userData);
    } catch (DataAccessException e) {
      // then
      assertEquals(e.getMessage(), "Error: bad reqeust");
    }
  }

  @Test
  void loginGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData regAuthData = userService.register(userData);

    // when
    AuthData logAuthData = userService.login("username", "password");

    // then
    assertEquals(logAuthData.username(), "username");
    assertEquals(logAuthData, authDataAccess.getAuthData(logAuthData.authToken()));
    assertNotEquals(regAuthData, logAuthData);
  }

  @Test
  void loginBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    userService.register(userData);

    // when
    try {
      userService.login("userName", "password");
    } catch (DataAccessException e) {
      // then
      assertEquals(e.getMessage(), "Error: unauthorized");
    }
  }

  @Test
  void logoutGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    userService.register(userData);
    AuthData loginAuthData = userService.login("username", "password");

    // when
    userService.logout(loginAuthData.authToken());

    // then
    try {
      authDataAccess.getAuthData(loginAuthData.authToken());
    } catch (DataAccessException e) {
      assertEquals("no autherization found", e.getMessage());
    }

  }

  @Test
  void logoutBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData regAuthData = userService.register(userData);

    // when
    userService.logout(regAuthData.authToken());

    // then
    try {
      authDataAccess.getAuthData(regAuthData.authToken());
    } catch (DataAccessException e) {
      assertEquals("no autherization found", e.getMessage());
    }
  }

  @Test
  void createGameGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = userService.register(userData);

    // when
    int gameId = gameService.createGame(authData.authToken(), "gameName");

    // then
    assertEquals(1, gameId);
  }

  @Test
  void creatGameBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    userService.register(userData);

    // when and then
    try {
      gameService.createGame("123", "gameName");
    } catch (DataAccessException e) {
      assertEquals("Error: unauthorized", e.getMessage());
    }
  }

  @Test
  void listGameGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = userService.register(userData);
    gameService.createGame(authData.authToken(), "gameName");

    // when
    Collection<GameSummaryData> games = gameService.listGames(authData.authToken());

    // then
    assertEquals(games.size(), 2);
  }

  @Test
  void listGameBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = userService.register(userData);
    gameService.createGame(authData.authToken(), "gameName");

    // when and then
    try {
      gameService.listGames("1234");
    } catch (DataAccessException e) {
      assertEquals("Error: unauthorized", e.getMessage());
    }
  }

  @Test
  void joinGameGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = userService.register(userData);
    int gameId = gameService.createGame(authData.authToken(), "gameName");

    // when
    gameService.joinGame(authData.authToken(), "WHITE", gameId);
    GameData gameData = gameService.getGame(authData.authToken(), gameId);

    // then
    assertEquals(gameData.whiteUsername(), userData.username());
  }

  @Test
  void joinGameBad() throws DataAccessException {
    UserData userData = new UserData("username", "password", "email");
    AuthData authData = userService.register(userData);
    int gameId = gameService.createGame(authData.authToken(), "gameName");
    gameService.joinGame(authData.authToken(), "WHITE", gameId);
    gameService.getGame(authData.authToken(), gameId);
    UserData user1Data = new UserData("username1", "password", "email");
    AuthData auth1Data = userService.register(user1Data);

    // when and then
    try {
      gameService.joinGame(auth1Data.authToken(), "WHITE", gameId);
    } catch (DataAccessException e) {
      assertEquals("Error: already taken", e.getMessage());
    }
  }

}
