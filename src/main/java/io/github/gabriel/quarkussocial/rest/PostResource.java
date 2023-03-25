package io.github.gabriel.quarkussocial.rest;

import io.github.gabriel.quarkussocial.domain.model.Post;
import io.github.gabriel.quarkussocial.domain.model.User;
import io.github.gabriel.quarkussocial.domain.repository.FollowerRepository;
import io.github.gabriel.quarkussocial.domain.repository.PostRepository;
import io.github.gabriel.quarkussocial.domain.repository.UserRepository;
import io.github.gabriel.quarkussocial.mapper.PostMapper;
import io.github.gabriel.quarkussocial.rest.dto.CreatePostRequest;
import io.github.gabriel.quarkussocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{idUser}/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {

    @Inject
    UserRepository userRepository;
    @Inject
    PostRepository postRepository;
    @Inject
    FollowerRepository followerRepository;

    @Inject
    PostMapper postMapper;

    @POST
    @Transactional
    public Response savePost(@PathParam("idUser") Long id, CreatePostRequest postRequest) {
        User user = userRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException());

        Post post = postMapper.toModel(postRequest, user);
        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listAll(@PathParam("idUser") Long id, @HeaderParam("followerId") Long followerId) {
        User user = userRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException());

        if (followerId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("You forgot the header followerId")
                    .build();
        }

        User follower = userRepository.findByIdOptional(followerId).orElseThrow(() -> new NotFoundException());

        boolean followers = followerRepository.followers(user, follower);

        if (!followers) {
            return Response.status(Response.Status.FORBIDDEN).entity("You can't see these posts").build();
        }

        PanacheQuery<Post> query = postRepository
                .find("user", Sort.by("date", Sort.Direction.Descending), user);
        List<PostResponse> responseList = query.stream().map(postMapper::toResponse).collect(Collectors.toList());
        return Response.ok(responseList).build();
    }
}
