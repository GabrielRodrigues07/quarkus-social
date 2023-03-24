package io.github.gabriel.quarkussocial.domain.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "fallowers")
@Data
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private User user;
    @Column(name = "follower_id")
    private User follower;
}
