package io.github.gabriel.quarkussocial.rest;

import io.github.gabriel.quarkussocial.domain.model.Follower;
import io.github.gabriel.quarkussocial.domain.model.User;
import io.github.gabriel.quarkussocial.domain.repository.FollowerRepository;
import io.github.gabriel.quarkussocial.domain.repository.UserRepository;
import io.github.gabriel.quarkussocial.rest.dto.FollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;

    Long userId;
    Long followerId;

    @BeforeEach
    @Transactional
    void setUp() {

        //usuario padrao dos teste
        var user = new User();
        user.setAge(30);
        user.setName("Fulano");

        userRepository.persist(user);
        userId = user.getId();

        //um serguidor
        var follower = new User();
        follower.setAge(45);
        follower.setName("Cicrano");

        userRepository.persist(follower);
        followerId = follower.getId();

        //cria um follower
        Follower followerEntity = new Follower();
        followerEntity.setFollower(follower);
        followerEntity.setUser(user);
        followerRepository.persist(followerEntity);

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

    @Test
    @DisplayName("should follow a user")
    public void followUserTest() {

        var body = new FollowerRequest();
        body.setFollowerId(followerId);

        given().contentType(ContentType.JSON)
                .body(body).pathParam("userId", userId)
                .when().put()
                .then().statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @DisplayName("should return 404 on list user followers and userId not exist")
    public void userNotFoundWhenListingFollowersTest() {

        var idUserInexistent = 999;

        given().contentType(ContentType.JSON)
                .pathParam("userId", idUserInexistent)
                .when().get()
                .then().statusCode(404);
    }

    @Test
    @DisplayName("should list a user followers")
    public void listFollowersTest() {

        var response = given().contentType(ContentType.JSON)
                .pathParam("userId", this.userId)
                .when().get()
                .then().extract().response();

        var count = response.jsonPath().getInt("followersCount");
        var followersContent = response.jsonPath().getList("content").size();

        Assertions.assertEquals(1, count);
        Assertions.assertEquals(1, followersContent);
        Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
    }
}