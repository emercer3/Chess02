package Service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;

public class AuthService {
  private final AuthDataAccess authDataAccess;

  public AuthService(AuthDataAccess authDataAccess) {
    this.authDataAccess = authDataAccess;
  }

  public void clearAuthData() throws DataAccessException {
    try {
      authDataAccess.clearAuthData();
    } catch (DataAccessException e) {
    }
  }

}
