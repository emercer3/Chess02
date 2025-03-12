package dataaccess.sqldataaccess;

import model.UserData;
import dataaccess.DataAccessException;

import java.sql.*;

public class UserSQLDataAccess implements dataaccess.UserDataAccess {

  private UserData readUserData(ResultSet rs) throws SQLException {
    var username = rs.getString("username");
    var passsword = rs.getString("password");
    var email = rs.getString("email");
    UserData userData = new UserData(username, passsword, email);
    return userData;
  }

  public UserSQLDataAccess() throws DataAccessException {
    DataBase.configureDatabase();
  }

  @Override
  public void createUser(UserData userData) throws DataAccessException {
    var statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
    DataBase.executeUpdate(statement, userData.username(), userData.password(), userData.email());
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
    DataBase.executeUpdate(statement, username);
  }

  @Override
  public void clearUserData() throws DataAccessException {
    var statement = "TRUNCATE userData";
    DataBase.executeUpdate(statement);
  }
}