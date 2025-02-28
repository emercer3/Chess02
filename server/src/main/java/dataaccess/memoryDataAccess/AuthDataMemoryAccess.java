package dataaccess.memoryDataAccess;

import model.AuthData;
import model.UserData;
import java.util.HashMap;
import java.util.UUID;

import dataaccess.DataAccessException;

public class AuthDataMemoryAccess implements dataaccess.AuthDataAccess {
  final private HashMap<String, String> autherizes = new HashMap<>();

  public static String generateToken() {
    return UUID.randomUUID().toString();
  }

  @Override
  public AuthData createAuth(String userName) throws DataAccessException {
    String authToken = generateToken();

    autherizes.put(authToken, userName); 
    return new AuthData(authToken, userName);
  }

  @Override
  public String geAuthData(String authToken) throws DataAccessException {
    if ( autherizes.containsKey(authToken) == false) {
      throw new DataAccessException("no autherization found");
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