package client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.*;

import exception.ResponseException;
import server.Server;
import server.ServerFacade;
import model.*;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port );
    }

    @BeforeEach
    public void clearServer() {
        try {
            facade.delete();
        } catch (ResponseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void register() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.register(user);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void login() throws Exception {
        // given
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.register(user);

        // when
        AuthData loginAuthData = facade.login(user.username(), user.password());

        // then
        assertEquals(loginAuthData.username(), "player1");
        assertNotEquals(authData, loginAuthData);
    }

    @Test
    void logout() throws Exception {
        // given
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.register(user);

        // when
        facade.logout(authData.authToken());

        // then
        try {
            facade.logout(authData.authToken());
        } catch (Exception e) {
            assertEquals("Cannot invoke \"java.lang.Double.intValue()\" because the return value of \"java.util.HashMap.get(Object)\" is null", e.getMessage());
        }
    }

    @Test
    void createGame() throws Exception {
        // given
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.register(user);

        // when
        int gameId = facade.createGame(authData.authToken(), "game1");

        // then
        assertEquals(1, gameId);
    }



}
