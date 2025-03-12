package service;

import dataaccess.UserDataAccess;

import org.mindrot.jbcrypt.BCrypt;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

public class UserService {
  private final UserDataAccess userDataAccess;
  private final AuthDataAccess authDataAccess;

  public UserService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
    this.userDataAccess = userDataAccess;
    this.authDataAccess = authDataAccess;
  }

  public void clearUserData() throws DataAccessException {
    try {
      userDataAccess.clearUserData();
    } catch (DataAccessException e) {
    }
  }

  public AuthData register(UserData userData) throws DataAccessException {
    if (userData.username() == null || userData.password() == null || userData.email() == null) {
      throw new DataAccessException("Error: bad request");
    }
    UserData existingUserData = null;
    try {
      existingUserData = userDataAccess.getUser(userData.username());
    } catch (DataAccessException e) {}

    if (existingUserData != null) {
      throw new DataAccessException("Error: already taken");
    }
    userDataAccess.createUser(userData);
    return authDataAccess.createAuth(userData.username());
  }

  public AuthData login(String userName, String password) throws DataAccessException {
    if (userName == null || password == null) {
      throw new DataAccessException("Error: bad request");
    }
    UserData userData;
    try {
      userData = userDataAccess.getUser(userName);
    } catch (DataAccessException e) {
      throw new DataAccessException("Error: unauthorized");
    }

    if (userData == null) {
      throw new DataAccessException("Error: unauthorized");
    }
    
    if (!BCrypt.checkpw(password, userData.password())) {
      throw new DataAccessException("Error: unauthorized");
    } else {
      return authDataAccess.createAuth(userName);
    }
  }

  public void logout(String authToken) throws DataAccessException {
    try {
      AuthData authData = authDataAccess.getAuthData(authToken);
      if (authData == null) {
        throw new DataAccessException("Error: unauthorized");
      }
      userDataAccess.deleteUserData(authData.username());
      authDataAccess.deleteAuth(authToken);
    } catch (DataAccessException e) {
      throw new DataAccessException("Error: unauthorized");
    }
  }
}
