package io.github.gabriel.quarkussocial.rest;

import io.github.gabriel.quarkussocial.domain.repository.FollowerRepository;
import io.github.gabriel.quarkussocial.domain.repository.UserRepository;
import io.github.gabriel.quarkussocial.mapper.FollowerMapper;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
}
