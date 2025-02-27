package model;

import java.util.UUID;

import com.google.gson.*;

public record AuthData(String authToken, String username) {

  public AuthData setAuth() {
    return new AuthData(authToken, this.username);
  }

  public String toString() {
    return new Gson().toJson(this);
  }
  
}