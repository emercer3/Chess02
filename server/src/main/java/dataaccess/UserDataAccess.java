package dataaccess;

import dataaccess.DataAccessException;
import model.UserData;

public interface UserDataAccess {
  void createUser(UserData userData) throws DataAccessException;  //return type authdata?

  UserData getUser(String username) throws DataAccessException;
  
  void deleteUserData(String username) throws DataAccessException;

  void clearUserData() throws DataAccessException;
}