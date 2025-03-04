package dataaccess;

import model.AuthData;

public interface AuthDataAccess {
  AuthData createAuth(String userName) throws DataAccessException;

  AuthData getAuthData(String authToken) throws DataAccessException;

  void deleteAuth(String authToken) throws DataAccessException;
  
  void clearAuthData() throws DataAccessException;

}