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

  public AuthData register(UserData userData) throws DataAccessException {
    if (userData.username() == null || userData.password() == null || userData.email() == null) {
      throw new DataAccessException("Error: bad reqeust");
    }

    try {
      userDataAccess.getUser(userData.username());
    } catch (DataAccessException e) {}
    
    userDataAccess.createUser(userData);
    return authDataAccess.createAuth(userData.username());
  }

  // public LoginResult Login(LoginRequest loginRequest) {}
  // public void logout(LogoutRequest logoutReuest) {}
}
