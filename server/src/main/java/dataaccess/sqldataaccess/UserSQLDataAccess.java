package dataaccess.sqldataaccess;

import model.UserData;
import dataaccess.DataAccessException;
// import dataaccess.sqldataaccess.DatabaseManager;

// import com.google.gson.Gson;
// import java.util.HashMap;
// import java.util.ArrayList;
// import java.util.Collection;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class UserSQLDataAccess implements dataaccess.UserDataAccess {

  private final String[] createStatements = {
    """
    CREATE TABLE IF NOT EXISTS userData (
      'username' CHAR(255) NOT NULL,
      'password' CHAR(255) NOT NULL,
      'email' varchar(256) NOT NULL,
      PRIMARY KEY ('username');
      INDEX(username);
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

  private UserData readUserData(ResultSet rs) throws SQLException {
    var username = rs.getString("username");
    var passsword = rs.getString("password");
    var email = rs.getString("email");
    UserData userData = new UserData(username, passsword, email);
    return userData;
  }

  public UserSQLDataAccess() throws DataAccessException {
    configureDatabase();
  }

  @Override
  public void createUser(UserData userData) throws DataAccessException {
    var statement = "INSERT INTO userData (username, password, email) VAUES (?, ?, ?)";
    executeUpdate(statement, userData.username(), userData.password(), userData.email());
  }

  @Override
  public UserData getUser(String username) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT username, password, email FROM userData WHERE username=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, username);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            return readUserData(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(e.getMessage());
    }
    return null;
  }

  @Override
  public void deleteUserData(String username) throws DataAccessException {
    var statement = "DELETE FROM userData WHERE username=?";
    executeUpdate(statement, username);
  }

  @Override
  public void clearUserData() throws DataAccessException {
    var statement = "TRUNCATE userData";
    executeUpdate(statement);
  }
}