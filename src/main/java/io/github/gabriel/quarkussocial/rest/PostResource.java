package io.github.gabriel.quarkussocial.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/{idUser}/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {

    @POST
    public Response savePost() {
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listAll() {
        return Response.ok().build();
    }
}
