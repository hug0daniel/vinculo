package com.vinculo.integration.api.user.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerAuthorizationIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void registerIsPublic() {
        String uniqueEmail = "newuser_" + System.currentTimeMillis() + "@test.com";
        
        String request = """
            {
                "email": "%s",
                "password": "password123",
                "userName": "newuser",
                "role": "VOLUNTEER"
            }
            """.formatted(uniqueEmail);

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post("/api/v1/users/register")
            .then()
            .statusCode(anyOf(is(201), is(409)));
    }

    @Test
    void loginIsPublic() {
        String request = """
            {
                "email": "test@test.com",
                "password": "wrongpassword"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post("/api/v1/users/login")
            .then()
            .statusCode(anyOf(is(200), is(401)));
    }

    @Test
    void getAllUsersRequiresAuth() {
        given()
            .when()
            .get("/api/v1/users")
            .then()
            .statusCode(403);
    }
}