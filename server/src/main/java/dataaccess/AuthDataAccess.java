package dataaccess;

import dataaccess.DataAccessException;
import model.AuthData;

import java.util.UUID;

public interface AuthDataAccess {
  String createAuth(AuthData authData) throws DataAccessException;

  AuthData geAuthData(String authToken) throws DataAccessException;
  
  void clearAuthData() throws DataAccessException;

}