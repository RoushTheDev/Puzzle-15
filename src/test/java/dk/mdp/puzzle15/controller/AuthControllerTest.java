package dk.mdp.puzzle15.controller;

import dk.mdp.puzzle15.dto.LoginRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void testLoginReturns200WithValidCredentials() {
        final LoginRequest loginRequest = new LoginRequest("player1", "pass1");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    void testLoginReturns400ForMissingUsername() {
        final String invalidPayload = """
                {
                  "password": "pass1"
                }
                """;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(invalidPayload)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("error", containsString("Bad Request"));
    }

    @Test
    void testLoginReturns401ForWrongPassword() {
        final LoginRequest loginRequest = new LoginRequest("player1", "wrongpass");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(401)
                .body("status", equalTo(401))
                .body("error", containsString("Unauthorized"));
    }

    @Test
    void testLoginReturns401ForUnknownUser() {
        final LoginRequest loginRequest = new LoginRequest("unknown", "whatever");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(401)
                .body("status", equalTo(401))
                .body("error", containsString("Unauthorized"));
    }
}
