package service;

import dataaccess.UserDataAccess;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.memoryDataAccess.UserDataMemoryAccess;
import dataaccess.memoryDataAccess.AuthDataMemoryAccess;
import model.UserData;
import model.AuthData;
import Service.AuthService;
import Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
  static final UserDataMemoryAccess userDataAccess = new UserDataMemoryAccess();
  static final AuthDataMemoryAccess authDataAccess = new AuthDataMemoryAccess();
  static final UserService userService = new UserService(userDataAccess, authDataAccess);
  static final AuthService authService = new AuthService(authDataAccess);

  @BeforeEach
  void clear() throws DataAccessException {
    userService.clearUserData();
  }

  @Test
  void registerGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");

    // when
    AuthData authData = userService.register(userData);

    // then
    assertEquals(userDataAccess.getUser("username"), userData);
    assertEquals(authData, authDataAccess.geAuthData(authData.authToken()));
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
    AuthData logAuthData = userService.Login("username", "password");

    // then
    assertEquals(logAuthData.username(), "username");
    assertEquals(logAuthData, authDataAccess.geAuthData(logAuthData.authToken()));
    assertNotEquals(regAuthData, logAuthData);
  }

  @Test
  void loginBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData regAuthData = userService.register(userData);

    // when
    try {
      AuthData logAuthData = userService.Login("userName", "password");
    } catch (DataAccessException e) {
    // then
      assertEquals(e.getMessage(), "Error: no user under that userName");
    }
  }

  @Test
  void logoutGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    AuthData regAuthData = userService.register(userData);
    AuthData loginAuthData = userService.Login("username", "password");

    // when
    userService.Logout(loginAuthData.authToken());

    // then
    try {
      authDataAccess.geAuthData(loginAuthData.authToken());
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
    userService.Logout(regAuthData.authToken());

    // then
    try {
      authDataAccess.geAuthData(regAuthData.authToken());
    } catch (DataAccessException e) {
      assertEquals("no autherization found", e.getMessage());
    }
  }

}
