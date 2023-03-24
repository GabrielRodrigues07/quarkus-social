package io.github.gabriel.quarkussocial.rest;

import io.github.gabriel.quarkussocial.domain.model.Follower;
import io.github.gabriel.quarkussocial.domain.model.User;
import io.github.gabriel.quarkussocial.domain.repository.FollowerRepository;
import io.github.gabriel.quarkussocial.domain.repository.UserRepository;
import io.github.gabriel.quarkussocial.mapper.FollowerMapper;
import io.github.gabriel.quarkussocial.rest.dto.FollowerRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


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
    public Response find() {
        return Response.ok().build();
    }

    @PUT
    @Transactional
    public Response followeUser(@PathParam("user_id") Long id, FollowerRequest request) {
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
}
