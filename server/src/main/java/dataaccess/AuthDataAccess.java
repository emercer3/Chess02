package dataaccess;

import dataaccess.DataAccessException;
import model.AuthData;

import java.util.UUID;

public interface AuthDataAccess {
  AuthData createAuth(String userName) throws DataAccessException;

  String geAuthData(String authToken) throws DataAccessException;

  void deleteAuth(String authToken) throws DataAccessException;
  
  void clearAuthData() throws DataAccessException;

}