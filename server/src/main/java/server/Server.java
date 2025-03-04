package server;

import com.google.gson.Gson;
import spark.Spark;
import spark.*;

import model.UserData;
import model.AuthData;
import dataaccess.memoryDataAccess.AuthDataMemoryAccess;
import dataaccess.memoryDataAccess.UserDataMemoryAccess;
import dataaccess.memoryDataAccess.GameDataMemoryAccess;
import Service.UserService;
import Service.GameService;
import Service.AuthService;
import dataaccess.DataAccessException;

public class Server {
    private final UserService userService;
    private final GameService gameService;
    private final AuthService authService;
    private final UserDataMemoryAccess userData;
    private final AuthDataMemoryAccess authData;
    private final GameDataMemoryAccess gameData;

    public Server() {
        this.userData = new UserDataMemoryAccess();
        this.authData = new AuthDataMemoryAccess();
        this.gameData = new GameDataMemoryAccess();
        this.userService = new UserService(userData, authData);
        this.gameService = new GameService(gameData, authData);
        this.authService = new AuthService(authData);
    }
    
    // public Server(UserService userService) {
    //     this.userService = userService;
    // }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::DeleteHandler);
        Spark.post("/user", this::RegisterHandler);
        Spark.post("/session", this::LoginHandler);
        Spark.delete("/session", this::LogoutHandler);
        Spark.post("/game", this::ListGamehandler);
    

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object DeleteHandler(Request req, Response res) throws DataAccessException {
        try {
            userService.clearUserData();
            gameService.clearGameData();
            authService.clearAuthData();
        } catch (DataAccessException e) {
            res.status(500);
            return new Gson().toJson("Error: " + e.getMessage());
        }

        res.status(200);
        return new Gson().toJson("");
    }

    private Object RegisterHandler(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        AuthData userauth = null;
        try {
            userauth = userService.register(user);
        } catch (DataAccessException e) {
            if (e.getMessage() == "Error: bad request") {
                res.status(400);
            } else if (e.getMessage() == "Error: already taken") {
                res.status(403);
            } else {
                res.status(500);
            }

            return new Gson().toJson(e.getMessage());
        }

        res.status(200);
        return new Gson().toJson(userauth);
    }

    private Object LoginHandler(Request req, Response res) throws DataAccessException {
        var userData = new Gson().fromJson(req.body(), UserData.class);
        AuthData userAuth = null;
        try {
            userAuth = userService.Login(userData.username(), userData.password());
        } catch (DataAccessException e) {
            if (e.getMessage() == "Error: unauthorized") {
                res.status(401);
            } else {
                res.status(500);
            }
            return new Gson().toJson(e.getMessage());
        }

        res.status(200);
        return userAuth;
    }

    private Object LogoutHandler(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");

        try {
           userService.Logout(authToken); 
        } catch (DataAccessException e) {
            if (e.getMessage() == "Error: unauthorized") {
                res.status(401);
            } else {
                res.status(500);
            }
            return new Gson().toJson(e.getMessage());
        }
        return new Gson().toJson("");
    }

    private Object ListGamehandler(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");

        try {
            gameService.listGames(authToken);
         } catch (DataAccessException e) {
             if (e.getMessage() == "Error: unauthorized") {
                 res.status(401);
             } else {
                 res.status(500);
             }
             return new Gson().toJson(e.getMessage());
         }
         return new Gson().toJson("");
     }

}
