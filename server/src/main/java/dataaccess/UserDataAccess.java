package dataaccess;

import dataaccess.DataAccessException;
import model.UserData;

import java.util.Collection;

public interface UserDataAccess {
  void createUser(UserData userData) throws DataAccessException;

  UserData getUser(String username) throws DataAccessException;
  
  void clearUserData(String username) throws DataAccessException;
}