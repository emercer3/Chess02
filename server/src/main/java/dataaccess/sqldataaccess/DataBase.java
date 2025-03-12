package dataaccess.sqldataaccess;

import model.UserData;
import dataaccess.DataAccessException;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DataBase {

  private static final String[] createStatements = {
    """
        CREATE TABLE IF NOT EXISTS userData (
          `username` varchar(255) NOT NULL,
          `password` varchar(255) NOT NULL,
          `email` varchar(256) NOT NULL,
          PRIMARY KEY (`username`),
          INDEX(`username`)
        )
        """,
  """
      CREATE TABLE IF NOT EXISTS gameData (
        `gameID` int NOT NULL AUTO_INCREMENT,
        `WhiteUsername` varchar(255),
        `BlackUsername` varchar(255),
        `gameName` varchar(255) NOT NULL,
        `game` TEXT NOT NULL,
        PRIMARY KEY (`gameID`),
        INDEX(`gameID`)
      )
      """,
  """
      CREATE TABLE IF NOT EXISTS authData (
        `authToken` CHAR(255) NOT NULL,
        `username` CHAR(255) NOT NULL,
        PRIMARY KEY (`authToken`),
        INDEX(`authToken`)
      )
      """
};

  static void configureDatabase() throws DataAccessException {
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

  static int executeUpdate(String statement, Object... params) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
        for (var i = 0; i < params.length; i++) {
          var param = params[i];
          if (param instanceof String p)
            ps.setString(i + 1, p);
          else if (param instanceof Integer p)
            ps.setInt(i + 1, p);
          else if (param == null)
            ps.setNull(i + 1, NULL);
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

}