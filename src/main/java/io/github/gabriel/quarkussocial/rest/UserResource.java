package io.github.gabriel.quarkussocial.rest;

import io.github.gabriel.quarkussocial.domain.model.User;
import io.github.gabriel.quarkussocial.domain.repository.UserRepository;
import io.github.gabriel.quarkussocial.mapper.UserMapper;
import io.github.gabriel.quarkussocial.rest.dto.CreateUserRequest;
import io.github.gabriel.quarkussocial.rest.dto.ResponseError;
import io.netty.handler.codec.http.HttpResponseStatus;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserMapper userMapper;

    @Inject
    UserRepository userRepository;

    @Inject
    Validator validator;


    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest){
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);
        if (!violations.isEmpty()) {
            return ResponseError.createFromValidation(violations).withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }
        User user = userMapper.toModel(userRequest);
        userRepository.persist(user);

        return Response.status(Response.Status.CREATED).entity(user).build();
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

        return Response.noContent().build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response update(@PathParam("id") Long id, CreateUserRequest userRequest) {
        User user = userRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Usuario não encontrado"));
        userMapper.update(userRequest, user);

        return Response.noContent().build();
    }
}
