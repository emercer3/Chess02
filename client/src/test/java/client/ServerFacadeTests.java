package client;

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

}
