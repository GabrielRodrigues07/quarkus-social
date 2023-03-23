package io.github.gabriel.quarkussocial.rest;

import io.github.gabriel.quarkussocial.domain.model.Post;
import io.github.gabriel.quarkussocial.domain.model.User;
import io.github.gabriel.quarkussocial.domain.repository.PostRepository;
import io.github.gabriel.quarkussocial.domain.repository.UserRepository;
import io.github.gabriel.quarkussocial.rest.dto.CreatePostRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/{idUser}/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {

    @Inject
    UserRepository userRepository;
    @Inject
    PostRepository postRepository;

    @POST
    @Transactional
    public Response savePost(@PathParam("idUser") Long id, CreatePostRequest postRequest) {
        User user = userRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException());

        Post post = new Post();
        post.setUser(user);
        post.setText(postRequest.getText());
        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listAll(@PathParam("idUser") Long id) {
        return Response.ok().build();
    }
}
