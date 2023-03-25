package io.github.gabriel.quarkussocial.rest.dto;

import io.github.gabriel.quarkussocial.domain.model.Follower;
import lombok.Data;

@Data
public class FollowerResponse {

    private Long id;
    private String name;

    public FollowerResponse() {
    }

    public FollowerResponse(Follower follower) {
        this.id = follower.getId();
        this.name = follower.getFollower().getName();
    }

    public FollowerResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
