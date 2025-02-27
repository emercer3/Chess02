package model;

import chess.ChessGame;
import com.google.gson.*;

public record GameData(int gameID, String whiteUsername, String blackUsername,
    String gameName, ChessGame game) {

  public GameData gameID(int gameID) {
    return new GameData(gameID, this.whiteUsername, this.blackUsername, this.gameName, this.game);
  }

  public String toString() {
    return new Gson().toJson(this);
  }

}