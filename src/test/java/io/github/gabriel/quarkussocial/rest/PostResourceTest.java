package io.github.gabriel.quarkussocial.rest;

import io.github.gabriel.quarkussocial.domain.model.Follower;
import io.github.gabriel.quarkussocial.domain.model.Post;
import io.github.gabriel.quarkussocial.domain.model.User;
import io.github.gabriel.quarkussocial.domain.repository.FollowerRepository;
import io.github.gabriel.quarkussocial.domain.repository.PostRepository;
import io.github.gabriel.quarkussocial.domain.repository.UserRepository;
import io.github.gabriel.quarkussocial.rest.dto.CreatePostRequest;
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

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;

    @Inject
    PostRepository postRepository;

    @BeforeEach
    @Transactional
    public void setUp() {
        //usuario padrao dos teste
        var user = new User();
        user.setAge(30);
        user.setName("Fulano");

        userRepository.persist(user);
        userId = user.getId();

        //postagem para o usuario
        Post post = new Post();
        post.setText("Hello");
        post.setUser(user);
        postRepository.persist(post);

        //usuario que nao segue ninguem
        var userNotFollower = new User();
        userNotFollower.setAge(45);
        userNotFollower.setName("Cicrano");
        userRepository.persist(userNotFollower);

        userNotFollowerId = userNotFollower.getId();

        //usuario que seguidor
        var userFollower = new User();
        userFollower.setAge(28);
        userFollower.setName("Beltrano");
        userRepository.persist(userFollower);

        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setFollower(userFollower);
        follower.setUser(user);

        followerRepository.persist(follower);
    }

    @Test
    @DisplayName("should create post for user")
    public void createPostTest() {
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        given().contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("idUser", userId)
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
                .pathParam("idUser", 99L)
                .when().post()
                .then().statusCode(404);
    }

    @Test
    @DisplayName("should return 404 when user doesn't exist")
    public void listPostUserNotFoundTest() {
        given().pathParam("idUser", 99L)
                .when().get()
                .then().statusCode(404);
    }

    @Test
    @DisplayName("should return 400 when followerId header is not present")
    public void listPostFollowerHeaderNotSendTest() {
        given().pathParam("idUser", userId)
                .when().get()
                .then().statusCode(400)
                .body(Matchers.is("You forgot the header followerId"));
    }

    @Test
    @DisplayName("should return 404 when follower doesn't exist")
    public void listPostFollowerNotFoundTest() {
        given().pathParam("idUser", userId)
                .header("followerId", 99L)
                .when().get()
                .then().statusCode(404);
    }

    @Test
    @DisplayName("should return 403 when follower isn't follower")
    public void listPostNotAFollowerTest() {
        given().pathParam("idUser", userId)
                .header("followerId", userNotFollowerId)
                .when().get()
                .then().statusCode(403)
                .body(Matchers.is("You can't see these posts"));
    }

    @Test
    @DisplayName("should return posts")
    public void listPostTest() {
        given().pathParam("idUser", userId)
                .header("followerId", userFollowerId)
                .when().get()
                .then().statusCode(200)
                .body("size()", Matchers.is(1));
    }
}