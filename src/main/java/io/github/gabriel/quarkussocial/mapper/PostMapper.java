package io.github.gabriel.quarkussocial.mapper;

import io.github.gabriel.quarkussocial.domain.model.Post;
import io.github.gabriel.quarkussocial.domain.model.User;
import io.github.gabriel.quarkussocial.rest.dto.CreatePostRequest;
import io.github.gabriel.quarkussocial.rest.dto.PostResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface PostMapper {

    Post toModel(CreatePostRequest postRequest, User user);

    PostResponse toResponse(Post post);
}
