package dataaccess.memoryDataAccess;

import model.UserData;
import java.util.HashMap;
import dataaccess.DataAccessException;

public class UserDataMemoryAccess implements dataaccess.UserDataAccess {
  final private HashMap<String, UserData> users = new HashMap<>();

  @Override
  public void createUser(UserData u) throws DataAccessException {
    if (users.containsKey(u.username())) {
      throw new DataAccessException("User is already exsits");
    }

    users.put(u.username(), u);
  }

  @Override
  public UserData getUser(String username) throws DataAccessException {
    if (users.containsKey(username) == false) {
      throw new DataAccessException("No user found, please create a new user");
    }

    return users.get(username);
  }

  @Override
  public void clearUserData(String username) throws DataAccessException {
    if (users.containsKey(username) == false) {
      throw new DataAccessException("user does not exsit");
    }

    users.remove(username);
  }
  
}