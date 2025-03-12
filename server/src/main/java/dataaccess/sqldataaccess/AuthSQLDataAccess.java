package dataaccess.sqldataaccess;

import model.AuthData;
import dataaccess.DataAccessException;

import java.util.UUID;
import java.sql.*;

public class AuthSQLDataAccess implements dataaccess.AuthDataAccess {

  private AuthData readAuthData(ResultSet rs) throws SQLException {
    var authToken = rs.getString("authToken");
    var username = rs.getString("username");
    AuthData authData = new AuthData(authToken, username);
    return authData;
  }

  public AuthSQLDataAccess() throws DataAccessException {
    DataBase.configureDatabase();
  }

  public static String generateToken() {
    return UUID.randomUUID().toString();
  }

  @Override
  public AuthData createAuth(String userName) throws DataAccessException {
    var statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
    String authToken = generateToken();
    DataBase.executeUpdate(statement, authToken, userName);
    return new AuthData(authToken, userName);
  }

  @Override
  public AuthData getAuthData(String authToken) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT authToken, username FROM authData WHERE authToken=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, authToken);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            return readAuthData(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(e.getMessage());
    }
    return null;
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {
    var statement = "DELETE FROM authData WHERE authToken=?";
    DataBase.executeUpdate(statement, authToken);
  }

  @Override
  public void clearAuthData() throws DataAccessException {
    var statement = "TRUNCATE authData";
    DataBase.executeUpdate(statement);
  }
}