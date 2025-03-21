package server;

import com.google.gson.Gson;
import exception.ResponseException;
import java.io.*;
import java.net.*;
import java.util.Collection;

import model.*;

public class ServerFacade {
  private final String serverUrl;

  public ServerFacade(String url) {
    serverUrl = url;
  }

  private <T> T makeRequest(String method, String path, String header, Object request, Class<T> responseClass) throws ResponseException {
    try {
      URI uri = new URI(serverUrl + path);
      HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
      http.setRequestMethod(method);
      http.setDoOutput(true);

      writeHeader(header, http);
      writeBody(request, http);

      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    } catch (ResponseException e) {
      throw e;
    } catch (Exception e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  private static void writeHeader(String header, HttpURLConnection http) throws IOException {
    if (header != null) {
      http.addRequestProperty("Authorization", header);
    }
  }

  private static void writeBody(Object request, HttpURLConnection http) throws IOException {
    if (request != null) {
      http.addRequestProperty("Content-Type", "application/json");
      String reqData = new Gson().toJson(request);
      try(OutputStream reqBody = http.getOutputStream()) {
        reqBody.write(reqData.getBytes());
      }
    }
  }

  private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      try (InputStream respErr = http.getErrorStream()) {
        if (respErr != null) {
          throw ResponseException.fromJson(respErr);
        }
      }
    }
  }

  private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
    T response = null;
    try (InputStream respBody = http.getInputStream()) {
      InputStreamReader reader = new InputStreamReader(respBody);
      String text = new String(respBody.readAllBytes());
      if (responseClass != null) {
        response = new Gson().fromJson(text, responseClass);
      }
    }
    return response;
  }

  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }

  public AuthData register(UserData userData) throws ResponseException {
    var path = "/user";
    return this.makeRequest("POST", path, null, userData, AuthData.class);
  }

  public AuthData login(String userName, String password) throws ResponseException {
    var path = "/session";
    UserData user = new UserData(userName, password, null);
    return this.makeRequest("POST", path, null, user, AuthData.class);
  }

  public void logout(String authToken) throws ResponseException {
    var path = "/session";
    this.makeRequest("DELETE", path, authToken, null, null);
  }

  public Collection<GameSummaryData> listGames(String authToken) throws ResponseException {
    var path = "/game";
    record listGamesResponse(Collection<GameSummaryData> games) {}
    var response = this.makeRequest("GET", path, authToken, null, listGamesResponse.class);
    return response.games();
  }

  public record gameName(String gameName) {
  }

  private record gameID(int gameID) {
  }

  public int createGame(String authToken, String gameName) throws ResponseException {
    var path = "/game";
    gameID gameId = this.makeRequest("POST", path, authToken, new gameName(gameName), gameID.class);
    return gameId.gameID();
  }

  private record JoinRequest(String playerColor, int gameID) {
  }

  public void joinGame(String authToken, String playerColor, int gameId) throws ResponseException {
    var path = "/game";

    this.makeRequest("PUT", path, authToken, new JoinRequest(playerColor, gameId), null);
  }

  public void delete() throws ResponseException {
    var path = "/db";
    this.makeRequest("DELETE", path, null, null, null);
  }




}
