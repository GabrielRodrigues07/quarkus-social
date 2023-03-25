package io.github.gabriel.quarkussocial.rest;

import io.github.gabriel.quarkussocial.rest.dto.CreateUserRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class UserResourceTest {

    CreateUserRequest user;

    @BeforeEach
    public void setUP() {
        user = new CreateUserRequest();
        user.setName("Fulano");
        user.setAge(30);
    }

    @Test
    @DisplayName("should create and user successfully")
    public void createUserTest() {
        var response = given().contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/users")
                .then().extract().response();
        assertEquals(201, response.getStatusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("Should return error when json is not valid")
    public void createUserValidationErrorTest() {
        user.setName(" ");
        user.setAge(null);
        var response = given().contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/users")
                .then()
                .extract().response();
        assertEquals(422, response.getStatusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));
    }
}