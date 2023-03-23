package io.github.gabriel.quarkussocial.rest;

import io.github.gabriel.quarkussocial.domain.model.User;
import io.github.gabriel.quarkussocial.domain.repository.UserRepository;
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

    @Inject
    UserRepository userRepository;

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {
        User user = userMapper.toModel(userRequest);
        userRepository.persist(user);

        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers() {
        return Response.ok(userRepository.findAll().list()).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        User user = userRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Usuario não encontrado"));
        userRepository.delete(user);

        return Response.ok().build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response update(@PathParam("id") Long id, CreateUserRequest userRequest) {
        User user = userRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Usuario não encontrado"));
        userMapper.update(userRequest, user);

        return Response.ok().build();
    }
}
