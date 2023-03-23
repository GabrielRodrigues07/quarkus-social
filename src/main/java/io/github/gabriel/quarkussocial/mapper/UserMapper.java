package io.github.gabriel.quarkussocial.mapper;

import io.github.gabriel.quarkussocial.domain.model.User;
import io.github.gabriel.quarkussocial.rest.dto.CreateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface UserMapper {

    User toModel(CreateUserRequest userRequest);

    void update(CreateUserRequest userRequest, @MappingTarget User user);
}
