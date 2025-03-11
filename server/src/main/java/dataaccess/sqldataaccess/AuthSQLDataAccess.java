package dataaccess.sqldataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.UUID;

import dataaccess.DataAccessException;

public class AuthSQLDataAccess implements dataaccess.AuthDataAccess {

  @Override
  public AuthData createAuth(String userName) throws DataAccessException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createAuth'");
  }

  @Override
  public AuthData getAuthData(String authToken) throws DataAccessException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAuthData'");
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {
    // var statement = "DELETE FROM authData WHERE username=?";
    // executeUpdate(statement, authToken);
    throw new UnsupportedOperationException("Unimplemented method 'createGame'");
  }

  @Override
  public void clearAuthData() throws DataAccessException {
    // var statement = "TRUNCATE authData";
    // executeUpdate(statement);
    throw new UnsupportedOperationException("Unimplemented method 'createGame'");
  }
}