package server;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;

import org.mindrot.jbcrypt.BCrypt;

import spark.*;

import model.UserData;
import model.AuthData;
import model.GameSummaryData;
import service.AuthService;
import service.GameService;
import service.UserService;
import dataaccess.DataAccessException;
import dataaccess.sqldataaccess.*;

public class Server {
    private final UserService userService;
    private final GameService gameService;
    private final AuthService authService;
    private final dataaccess.UserDataAccess userData;
    private final dataaccess.AuthDataAccess authData;
    private final dataaccess.GameDataAccess gameData;

    public Server() {
        
        try {
            this.userData = new UserSQLDataAccess();
            this.authData = new AuthSQLDataAccess();
            this.gameData = new GameSQLDataAccess();
        } catch (Exception e) {
            throw new RuntimeException();
        }

        this.userService = new UserService(userData, authData);
        this.gameService = new GameService(gameData, authData);
        this.authService = new AuthService(authData);
    }

    private String encryptpassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private record MyError(String message) {
    }

    private record GameId(int gameID) {
    }

    private record JoinRequest(String playerColor, int gameID) {
    }

    private record ListGames(Collection<GameSummaryData> games) {
    }

    private record CreateGame(String gameName) {
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::deleteHandler);
        Spark.post("/user", this::registerHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.get("/game", this::listGamehandler);
        Spark.post("/game", this::createGameHandler);
        Spark.put("/game", this::joinGameHandler);

        // This line initializes the server and can be removed once you have a
        // functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object deleteHandler(Request req, Response res) throws DataAccessException {
        try {
            userService.clearUserData();
            gameService.clearGameData();
            authService.clearAuthData();
        } catch (DataAccessException e) {
            res.status(500);
            return new Gson().toJson("Error: " + e.getMessage());
        }

        res.status(200);
        return "{}";
    }

    private Object registerHandler(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        UserData newUserData = null;
        AuthData userAuth = null;

        if (user.password() == null) {
            newUserData = new UserData(user.username(), null, user.email());
        } else {
            newUserData = new UserData(user.username(), encryptpassword(user.password()), user.email());
        }

        try {
            userAuth = userService.register(newUserData);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: bad request")) {
                res.status(400);
            } else if (e.getMessage() == "Error: already taken") {
                res.status(403);
            } else {
                res.status(500);
            }

            return new Gson().toJson(new MyError(e.getMessage()));
        }

        res.status(200);
        return new Gson().toJson(userAuth);
    }

    private Object loginHandler(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        // UserData userData = new UserData(user.username(),
        // encryptpassword(user.password()),user.email());
        AuthData userAuth = null;
        try {
            userAuth = userService.login(user.username(), user.password());
        } catch (DataAccessException e) {
            return getErrorMessage(e, res);
        }

        res.status(200);
        return new Gson().toJson(userAuth);
    }

    private Object getErrorMessage(DataAccessException e, Response res) {
        if (e.getMessage() == "Error: unauthorized") {
            res.status(401);
        } else {
            res.status(500);
        }
        return new Gson().toJson(new MyError(e.getMessage()));
    }

    private Object logoutHandler(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");

        try {
            userService.logout(authToken);
        } catch (DataAccessException e) {
            return getErrorMessage(e, res);
        }
        res.status(200);
        return "{}";
    }

    private Object listGamehandler(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        Collection<GameSummaryData> games = new ArrayList<>();
        try {
            games = gameService.listGames(authToken);
        } catch (DataAccessException e) {
            return getErrorMessage(e, res);
        }

        res.status(200);
        return new Gson().toJson(new ListGames(games));
    }

    private Object createGameHandler(Request req, Response res) throws DataAccessException {
        CreateGame gameName = new Gson().fromJson(req.body(), CreateGame.class);
        String authToken = req.headers("authorization");
        int gameId;
        try {
            gameId = gameService.createGame(authToken, gameName.gameName);
        } catch (DataAccessException e) {
            if (e.getMessage() == "Error: bad request") {
                res.status(400);
            } else if (e.getMessage() == "Error: unauthorized") {
                res.status(401);
            } else {
                res.status(500);
            }
            return new Gson().toJson(new MyError(e.getMessage()));
        }
        res.status(200);
        return new Gson().toJson(new GameId(gameId));
    }

    private Object joinGameHandler(Request req, Response res) throws DataAccessException {
        JoinRequest gameinfo = new Gson().fromJson(req.body(), JoinRequest.class);
        String authToken = req.headers("authorization");

        try {
            gameService.joinGame(authToken, gameinfo.playerColor, gameinfo.gameID);
        } catch (DataAccessException e) {
            if (e.getMessage() == "Error: bad request") {
                res.status(400);
            } else if (e.getMessage() == "Error: unauthorized") {
                res.status(401);
            } else if (e.getMessage() == "Error: already taken") {
                res.status(403);
            } else {
                res.status(500);
            }
            return new Gson().toJson(new MyError(e.getMessage()));
        }
        res.status(200);
        return "{}";
    }

}
