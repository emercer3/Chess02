package dataaccess.memoryDataAccess;

import model.AuthData;
import java.util.HashMap;
import java.util.UUID;

import dataaccess.DataAccessException;

public class AuthDataMemoryAccess implements dataaccess.AuthDataAccess {
  final private HashMap<String, AuthData> autherizes = new HashMap<>();

  public static String generateToken() {
    return UUID.randomUUID().toString();
  }

  @Override
  public AuthData createAuth(String userName) throws DataAccessException {
    String authToken = generateToken();
    AuthData authData = new AuthData(authToken, userName);
    autherizes.put(authToken, authData);
    return authData;
  }

  @Override
  public AuthData getAuthData(String authToken) throws DataAccessException {
    if (autherizes.containsKey(authToken) == false) {
      throw new DataAccessException("Error: unauthorized");
    }

    return autherizes.get(authToken);
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {
    if (autherizes.containsKey(authToken) == false) {
      throw new DataAccessException("No user found to delete");
    }

    autherizes.remove(authToken);
  }

  @Override
  public void clearAuthData() throws DataAccessException {
    autherizes.clear();
  }
}