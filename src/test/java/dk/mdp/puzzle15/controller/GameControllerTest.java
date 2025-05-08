package dk.mdp.puzzle15.controller;

import dk.mdp.puzzle15.dto.LoginRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameControllerTest {

    @LocalServerPort
    private int port;

    private String tokenPlayer1;
    private String tokenPlayer2;
    private String tokenAdmin;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        tokenPlayer1 = login("player1", "pass1");
        tokenPlayer2 = login("player2", "pass2");
        tokenAdmin = login("admin", "adminpass");
    }

    private String login(final String username, final String password) {
        return given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(username, password))
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @Test
    void testStartGameReturns200() {
        given()
                .auth().oauth2(tokenPlayer1)
                .when()
                .post("/api/game/start")
                .then()
                .statusCode(200)
                .body("gameId", notNullValue())
                .body("board", notNullValue());
    }

    @Test
    void testStartGameReturns403WithoutToken() {
        when()
                .post("/api/game/start")
                .then()
                .statusCode(403);
    }

    @Test
    void testGetGameReturns200ForValidUser() {
        final UUID gameId = createGame(tokenPlayer1);

        given()
                .auth().oauth2(tokenPlayer1)
                .when()
                .get("/api/game/{gameId}", gameId)
                .then()
                .statusCode(200)
                .body("gameId", equalTo(gameId.toString()));
    }

    @Test
    void testGetGameReturns403ForWrongUser() {
        final UUID gameId = createGame(tokenPlayer1);

        given()
                .auth().oauth2(tokenPlayer2)
                .when()
                .get("/api/game/{gameId}", gameId)
                .then()
                .statusCode(403);
    }

    @Test
    void testGetGameReturns404ForMissingGame() {
        given()
                .auth().oauth2(tokenPlayer1)
                .when()
                .get("/api/game/{gameId}", UUID.randomUUID())
                .then()
                .statusCode(404);
    }

    @Test
    void testMoveReturns200ForValidMove() {
        final UUID gameId = createGame(tokenPlayer1);

        given()
                .auth().oauth2(tokenPlayer1)
                .contentType(ContentType.JSON)
                .body(Map.of("direction", "RIGHT"))
                .when()
                .put("/api/game/{gameId}/move", gameId)
                .then()
                .statusCode(200)
                .body("gameId", equalTo(gameId.toString()));
    }

    @Test
    void testMoveReturns400ForMissingDirection() {
        final UUID gameId = createGame(tokenPlayer1);

        given()
                .auth().oauth2(tokenPlayer1)
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .put("/api/game/{gameId}/move", gameId)
                .then()
                .statusCode(400);
    }

    @Test
    void testMoveReturns403ForWrongUser() {
        final UUID gameId = createGame(tokenPlayer1);

        given()
                .auth().oauth2(tokenPlayer2)
                .contentType(ContentType.JSON)
                .body(Map.of("direction", "RIGHT"))
                .when()
                .put("/api/game/{gameId}/move", gameId)
                .then()
                .statusCode(403);
    }

    @Test
    void testMoveReturns404ForUnknownGame() {
        given()
                .auth().oauth2(tokenPlayer1)
                .contentType(ContentType.JSON)
                .body(Map.of("direction", "RIGHT"))
                .when()
                .put("/api/game/{gameId}/move", UUID.randomUUID())
                .then()
                .statusCode(404);
    }

    @Test
    void testTerminateReturns204ForAdmin() {
        final UUID gameId = createGame(tokenPlayer1);

        given()
                .auth().oauth2(tokenAdmin)
                .when()
                .delete("/api/game/{gameId}", gameId)
                .then()
                .statusCode(204);
    }

    @Test
    void testTerminateReturns403ForRegularUser() {
        final UUID gameId = createGame(tokenPlayer1);

        given()
                .auth().oauth2(tokenPlayer1)
                .when()
                .delete("/api/game/{gameId}", gameId)
                .then()
                .statusCode(403);
    }

    private UUID createGame(final String token) {
        return UUID.fromString(
                given()
                        .auth().oauth2(token)
                        .when()
                        .post("/api/game/start")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("gameId")
        );
    }
}
