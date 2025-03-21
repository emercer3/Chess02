package client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        facade = new ServerFacade("http://localhost:" + port);
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
    void delete() throws Exception {
        // given
        UserData user = new UserData("player1", "password", "p1@email.com");
        facade.register(user);

        // when
        facade.delete();

        // then
        assertThrows(ResponseException.class, () -> facade.login(user.username(), user.password()));
    }

    @Test
    void deleteBad() throws Exception {
        // given nothing
        // when and then
        assertDoesNotThrow(() -> facade.delete(), "error");
    }

    @Test
    void register() throws Exception {
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.register(user);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerBad() throws Exception {
        // given
        UserData user = new UserData("player1", null, "p1@email.com");

        // when and then
        try {
            facade.register(user);
        } catch (ResponseException e) {
            assertEquals(e.getMessage(), "Error: bad request");
        }
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
    void loginBad() throws Exception {
        // given
        UserData user = new UserData("player1", "password", "p1@email.com");
        facade.register(user);

        // when and then
        try {
            facade.login(user.username(), "pasword");
        } catch (ResponseException e) {
            assertEquals(e.getMessage(), "Error: unauthorized");
        }

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
            assertEquals("Error: unauthorized", e.getMessage());
        }
    }

    @Test
    void logoutBad() throws Exception {
        // given
        // when and then
        try {
            facade.logout(null);
        } catch (Exception e) {
            assertEquals("Error: unauthorized", e.getMessage());
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

    @Test
    void createGameBad() throws Exception {
        // given
        new UserData("player1", "password", "p1@email.com");

        // when and try
        assertThrows(ResponseException.class, () -> facade.createGame("1234", "game1"));
    }

    @Test
    void listGames() throws Exception {
        // given
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.register(user);
        facade.createGame(authData.authToken(), "game1");
        facade.createGame(authData.authToken(), "game2");

        // when
        Collection<GameSummaryData> gameList = facade.listGames(authData.authToken());
        List<GameSummaryData> gameListAsList = new ArrayList<>(gameList);

        // then
        assertEquals(gameListAsList.size(), 2);
        assertEquals(gameListAsList.get(0).gameName(), "game1");
        assertEquals(gameListAsList.get(1).gameName(), "game2");
    }

    @Test
    void listGamesBad() throws Exception {
        // given
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.register(user);
        facade.createGame(authData.authToken(), "game1");

        // when and then
        assertThrows(ResponseException.class, () -> facade.listGames("123"));
    }

    @Test
    void joinGame() throws Exception {
        // given
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.register(user);
        int gameId = facade.createGame(authData.authToken(), "game1");

        // when and then
        assertDoesNotThrow(() -> facade.joinGame(authData.authToken(), "WHITE", gameId), "oops got an exception");
    }

    @Test
    void joinGameBad() throws Exception {
        // given
        UserData user = new UserData("player1", "password", "p1@email.com");
        var authData = facade.register(user);
        int gameId = facade.createGame(authData.authToken(), "game1");

        // when and then
        assertThrows(ResponseException.class, () -> facade.joinGame("1234", "WHITE", gameId));
    }

}
