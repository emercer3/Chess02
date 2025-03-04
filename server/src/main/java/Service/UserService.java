package Service;

import dataaccess.UserDataAccess;
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
    } catch (DataAccessException e) {}
  }

  public AuthData register(UserData userData) throws DataAccessException {
    if (userData.username() == null || userData.password() == null || userData.email() == null) {
      throw new DataAccessException("Error: bad request");
    }

    try {
      userDataAccess.getUser(userData.username());
    } catch (DataAccessException e) {}
    
    userDataAccess.createUser(userData);
    return authDataAccess.createAuth(userData.username());
  }

  public AuthData Login(String userName, String password) throws DataAccessException{
    if (userName == null || password == null) {
      throw new DataAccessException("Error: bad request");
    }
    UserData userData;
    try {
      userData = userDataAccess.getUser(userName);
    } catch (DataAccessException e) {
      throw new DataAccessException("Error: unauthorized");
    }

    if (!userData.password().equals(password)) {
      throw new DataAccessException("Error: unauthorized");
    } else {
      return authDataAccess.createAuth(userName);
    }
  }

  public void Logout(String authToken) throws DataAccessException {
    try {
      AuthData authData = authDataAccess.getAuthData(authToken);
      userDataAccess.deleteUserData(authData.username());
    } catch (DataAccessException e) {
      throw new DataAccessException("Error: unauthorized");
    }
  }
}
