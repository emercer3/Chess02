package model;

import com.google.gson.*;

public record GameSummaryData(int gameID, String whiteUsername, String blackUsername,
    String gameName) {

  public GameSummaryData gameID(int gameID) {
    return new GameSummaryData(gameID, this.whiteUsername, this.blackUsername, this.gameName);
  }

  public String toString() {
    return new Gson().toJson(this);
  }

}
