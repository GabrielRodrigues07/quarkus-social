package io.github.gabriel.quarkussocial.rest.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {

    private String text;
    private LocalDateTime date;
}
