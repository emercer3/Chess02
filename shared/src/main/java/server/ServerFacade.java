package server;

import com.google.gson.Gson;
// import exception.ErrorResponse;
// import exception.ResponseException;
import java.io.*;
import java.net.*;
import java.util.Collection;

import model.*;

public class ServerFacade {
  private final String serverUrl;

  public ServerFacade(String url) {
    serverUrl = url;
  }

  private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
    try{
      URI uri = new URI(serverUrl);
      HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
      http.setRequestMethod(method);
      http.setDoOutput(true);

      writeBody(request, http);
      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    } catch (Exception e) {
      throw e;
    } 
    // catch (Exception e) {
    //   throw new Exception(500, e.getMessage());
    // }
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

  private void throwIfNotSuccessful(HttpURLConnection http) throws IOException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      try (InputStream respErr = http.getErrorStream()) {
        if (respErr != null) {
          throw Exception.fromJson(respErr); // need to make exception class
        }
      }
    }
  }

  private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
    T response = null;
    if (http.getContentLength() < 0) {
      try (InputStream respBody = http.getInputStream()) {
        InputStreamReader reader = new InputStreamReader(respBody);
        if (responseClass != null) {
          response = new Gson().fromJson(reader, responseClass);
        }
      }
    }
    return response;
  }

  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }

  public AuthData register(UserData userData) throws Exception {
    var path = "/user";
    return this.makeRequest("POST", path, userData, AuthData.class);
  }

  public AuthData login(String userName, String password) throws Exception {
    var path = "/session";
    return this.makeRequest("POST", path, userName, password, AuthData.class);
  }

  public void logout(String authToken) throws Exception {
    var path = "/session";
    this.makeRequest("DELETE", path, authToken, null);
  }

  public Collection<GameSummaryData> listGames(String authToken) throws Exception {
    var path = "/game";
    record listGamesResponse(Collection<GameSummaryData> games) {}
    var response = this.makeRequest("GET", path, authToken, listGamesResponse.class);
    return response.games();
  }

  public int createGame(String authToken, String gameName) throws Exception {
    var path = "/game";
    return this.makeRequest("POST", path, authToken, gameName, int.class);
  }

  public void joinGame(String authToken, String gameName) throws Exception {
    var path = "/game";
    this.makeRequest("PUT", path, authToken, gameName, null);
  }

  public void delete() throws Exception {
    var path = "/db";
    this.makeRequest("DELETE", path, null, null);
  }




}
