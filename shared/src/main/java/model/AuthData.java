package model;

import com.google.gson.*;

public record AuthData(String authToken, String username) {

  public AuthData setAuth() {
    return new AuthData(this.authToken, this.username);
  }

  public String toString() {
    return new Gson().toJson(this);
  }

}