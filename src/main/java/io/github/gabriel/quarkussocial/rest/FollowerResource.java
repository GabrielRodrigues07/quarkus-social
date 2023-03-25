package io.github.gabriel.quarkussocial.rest;

import io.github.gabriel.quarkussocial.domain.model.Follower;
import io.github.gabriel.quarkussocial.domain.model.User;
import io.github.gabriel.quarkussocial.domain.repository.FollowerRepository;
import io.github.gabriel.quarkussocial.domain.repository.UserRepository;
import io.github.gabriel.quarkussocial.mapper.FollowerMapper;
import io.github.gabriel.quarkussocial.rest.dto.FollowerRequest;
import io.github.gabriel.quarkussocial.rest.dto.FollowerResponse;
import io.github.gabriel.quarkussocial.rest.dto.FollowersPerUseResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;


@Path("/users/{user_id}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    @Inject
    FollowerMapper followerMapper;

    @Inject
    FollowerRepository followerRepository;

    @Inject
    UserRepository userRepository;

    @GET
    public Response listFollowers(@PathParam("user_id") Long userId) {
        userRepository.findByIdOptional(userId).orElseThrow(NotFoundException::new);

        List<Follower> followers = followerRepository.findByUser(userId);

        FollowersPerUseResponse perUseResponse = new FollowersPerUseResponse();
        perUseResponse.setFollowersCount(followers.size());

//        followers.stream()
//                .map(follower -> new FollowerResponse(follower.getId(), follower.getFollower().getName()))
//                .collect(Collectors.toList());

        // method reference
        List<FollowerResponse> followerResponses = followers.stream().map(FollowerResponse::new).collect(Collectors.toList());
        perUseResponse.setContent(followerResponses);
        return Response.ok(perUseResponse).build();
    }

    @PUT
    @Transactional
    public Response followeUser(@PathParam("user_id") Long id, FollowerRequest request) {

        if (id.equals(request.getFollowerId())) {
            return Response.status(Response.Status.CONFLICT).entity("you can't follow yourself").build();
        }

        User user = userRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
        User follower = userRepository.findById(request.getFollowerId());

        boolean followers = followerRepository.followers(user, follower);

        if (!followers) {
            var entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);

            followerRepository.persist(entity);
        }

        return Response.noContent().build();
    }

    @DELETE
    @Transactional
    public Response unFollowUser(@PathParam("user_id") Long userId, @QueryParam("followerId") Long followerId) {
        userRepository.findByIdOptional(userId).orElseThrow(NotFoundException::new);

        followerRepository.deleteByFollowerAndUser(followerId, userId);


        return Response.noContent().build();
    }
}
