package server;

import com.google.gson.Gson;
import spark.Spark;
import spark.*;

import model.UserData;
import model.AuthData;
import dataaccess.memoryDataAccess.AuthDataMemoryAccess;
import dataaccess.memoryDataAccess.UserDataMemoryAccess;
import Handler.UserHandler;
import Service.UserService;
import dataaccess.DataAccessException;

public class Server {
    private final UserService userService;
    private final UserDataMemoryAccess userData;
    private final AuthDataMemoryAccess authData;

    public Server() {
        this.userData = new UserDataMemoryAccess();
        this.authData = new AuthDataMemoryAccess();
        this.userService = new UserService(userData, authData);
    }
    
    // public Server(UserService userService) {
    //     this.userService = userService;
    //     userHandler = new UserHandler();
    // }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::RegisterHandler);
    

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
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


}
