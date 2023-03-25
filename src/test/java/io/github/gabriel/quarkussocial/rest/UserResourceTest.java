package io.github.gabriel.quarkussocial.rest;

import io.github.gabriel.quarkussocial.rest.dto.CreateUserRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@DisplayName("should create and user successfully")
class UserResourceTest {

    @Test
    public void createUserTest() {
        var user = new CreateUserRequest();
        user.setName("Fulano");
        user.setAge(30);
        var response = RestAssured.given().contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/users")
                .then().extract().response();
        assertEquals(201, response.getStatusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }
}