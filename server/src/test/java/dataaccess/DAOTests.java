package dataaccess;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    //then
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

    //then
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

    //then
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
    } catch (Exception e) {}
    assertEquals(oldUserData , null);
    
  }

  @Test
  void deleteUserDataGood() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");
    userSQL.createUser(userData);

    // when
    userSQL.deleteUserData(userData.username());

    //then
    UserData existingUserData = userSQL.getUser(userData.username());
    assertEquals(existingUserData, null);

  }

  @Test
  void deleteUserDataBad() throws DataAccessException {
    // given
    UserData userData = new UserData("username", "password", "email");

    // when
    try {
      userSQL.deleteUserData("Username");
    } catch (Exception e) {
      // then
      assertEquals(e.getMessage(),"error");
    }
    
  }

}