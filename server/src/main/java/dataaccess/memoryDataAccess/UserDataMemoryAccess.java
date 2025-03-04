package dataaccess.memorydataaccess;

import model.UserData;
import java.util.HashMap;
import dataaccess.DataAccessException;

public class UserDataMemoryAccess implements dataaccess.UserDataAccess {
  final private HashMap<String, UserData> users = new HashMap<>();

  @Override
  public void createUser(UserData u) throws DataAccessException {
    if (users.containsKey(u.username())) {
      throw new DataAccessException("Error: already taken");
    }

    users.put(u.username(), u);
  }

  @Override
  public UserData getUser(String username) throws DataAccessException {
    if (users.containsKey(username) == false) {
      throw new DataAccessException("Error: No user found, please create a new user");
    }

    return users.get(username);
  }

  @Override
  public void deleteUserData(String username) throws DataAccessException {
    if (users.containsKey(username) == false) {
      throw new DataAccessException("Error: user does not exsit");
    }

    users.remove(username);
  }

  @Override
  public void clearUserData() throws DataAccessException {
    users.clear();
  }

}