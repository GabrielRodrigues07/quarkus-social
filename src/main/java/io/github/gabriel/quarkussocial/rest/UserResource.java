package io.github.gabriel.quarkussocial.rest;

import io.github.gabriel.quarkussocial.domain.model.User;
import io.github.gabriel.quarkussocial.mapper.UserMapper;
import io.github.gabriel.quarkussocial.rest.dto.CreateUserRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserMapper userMapper;

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {
        User user = userMapper.toModel(userRequest);
        user.persist();

        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers() {
        return Response.ok(User.findAll().list()).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        User.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Usuario não encontrado")).delete();
        return Response.ok().build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response update(@PathParam("id") Long id, CreateUserRequest userRequest) {
        User user = User.findById(id);
        if (user != null) {
            userMapper.update(userRequest, user);
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
