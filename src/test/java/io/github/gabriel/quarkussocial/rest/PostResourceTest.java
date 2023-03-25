package io.github.gabriel.quarkussocial.rest;

import io.github.gabriel.quarkussocial.domain.model.User;
import io.github.gabriel.quarkussocial.domain.repository.UserRepository;
import io.github.gabriel.quarkussocial.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRepository userRepository;

    @BeforeEach
    @Transactional
    public void setUp() {
        var user = new User();
        user.setAge(30);
        user.setName("Fulano");

        userRepository.persist(user);
    }

    @Test
    @DisplayName("should create post for user")
    public void createPostTest() {
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        given().contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("idUser", 1L)
                .when().post()
                .then().statusCode(201);
    }

    @Test
    @DisplayName("should return 404 when try find")
    public void return404WhenTryFind() {
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        given().contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("idUser", 2L)
                .when().post()
                .then().statusCode(404);
    }

    @Test
    @DisplayName("should return 404 when user doesn't exist")
    public void listPostUserNotFoundTest() {

    }

    @Test
    @DisplayName("should return 400 when followerId header is not present")
    public void listPostFollowerHeaderNotSendTest() {

    }

    @Test
    @DisplayName("should return 404 when follower doesn't exist")
    public void listPostFollowerNotFoundTest() {

    }

    @Test
    @DisplayName("should return 403 when follower isn't follower")
    public void listPostNotAFollowerTest() {

    }

    @Test
    @DisplayName("should return posts")
    public void listPostTest() {

    }
}