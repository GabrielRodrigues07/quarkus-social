package io.github.gabriel.quarkussocial.rest;

import io.github.gabriel.quarkussocial.domain.model.User;
import io.github.gabriel.quarkussocial.domain.repository.UserRepository;
import io.github.gabriel.quarkussocial.rest.dto.FollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

    @Inject
    UserRepository userRepository;

    Long userId;

    @BeforeEach
    @Transactional
    void setUp() {

        //usuario padrao dos teste
        var user = new User();
        user.setAge(30);
        user.setName("Fulano");

        userRepository.persist(user);
        userId = user.getId();
    }

    @Test
    @DisplayName("should return 409 when followerId is equal userId")
    public void sameUserAsFollowerTest() {

        var body = new FollowerRequest();
        body.setFollowerId(userId);

        given().contentType(ContentType.JSON)
                .body(body).pathParam("userId", userId)
                .when().put()
                .then().statusCode(409)
                .body(Matchers.is("you can't follow yourself"));
    }

    @Test
    @DisplayName("should return 404 when userId not exist")
    public void userNotFoundTest() {

        var idUserInexistent = 999;

        var body = new FollowerRequest();
        body.setFollowerId(userId);

        given().contentType(ContentType.JSON)
                .body(body).pathParam("userId", idUserInexistent)
                .when().put()
                .then().statusCode(404);
    }
}