package dataaccess.memoryDataAccess;

import model.AuthData;
import model.UserData;
import java.util.HashMap;
import java.util.UUID;

import dataaccess.DataAccessException;

public class AuthDataMemoryAccess implements dataaccess.AuthDataAccess {
  final private HashMap<String, AuthData> autherizes = new HashMap<>();

  @Override
  public String createAuth(AuthData authData) throws DataAccessException {
    String autherization = new authData.generateToken();

    autherizes.put(autherization, authData);

  }

  @Override
  public AuthData geAuthData(String authToken) throws DataAccessException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'geAuthData'");
  }

  @Override
  public void clearAuthData() throws DataAccessException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'clearAuthData'");
  }
}