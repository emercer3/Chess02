package dataaccess.sqldataaccess;

import model.AuthData;
import dataaccess.DataAccessException;

// import com.google.gson.Gson;
// import java.util.HashMap;
import java.util.UUID;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class AuthSQLDataAccess implements dataaccess.AuthDataAccess {

  private final String[] createStatements = {
    """
    CREATE TABLE IF NOT EXISTS userData (
      'authToken' CHAR(255) NOT NULL,
      'username' CHAR(255) NOT NULL,
      PRIMARY KEY ('authToken');
      INDEX(authToken);
    )
    """
  };

  private void configureDatabase() throws DataAccessException{
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(e.getMessage());
    }

  }

  private int executeUpdate(String statement, Object... params) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try ( var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
        for (var i = 0; i < params.length; i++) {
          var param = params[i];
          if (param instanceof String p) ps.setString(i + 1, p);
          else if (param instanceof Integer p) ps.setInt(i + 1, p);
          else if (param == null) ps.setNull(i + 1, NULL);
        }
        ps.executeUpdate();

        var rs = ps.getGeneratedKeys();
        if (rs.next()) {
          return rs.getInt(1);
        }

        return 0;
      }
    } catch (Exception e) {
      throw new DataAccessException(e.getMessage());
    }
  }

  private AuthData readAuthData(ResultSet rs) throws SQLException {
    var authToken = rs.getString("authToken");
    var username = rs.getString("username");
    AuthData authData = new AuthData(authToken, username);
    return authData;
  }

  public AuthSQLDataAccess() throws DataAccessException {
    configureDatabase();
  }

  public static String generateToken() {
    return UUID.randomUUID().toString();
  }

  @Override
  public AuthData createAuth(String userName) throws DataAccessException {
    var statement = "INSERT INTO authData (authToken, username) VAUES (?, ?)";
    String authToken = generateToken();
    executeUpdate(statement, authToken, userName);
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
    executeUpdate(statement, authToken);
  }

  @Override
  public void clearAuthData() throws DataAccessException {
    var statement = "TRUNCATE authData";
    executeUpdate(statement);
  }
}