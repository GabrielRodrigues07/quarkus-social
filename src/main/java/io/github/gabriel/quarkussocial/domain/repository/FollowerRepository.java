package io.github.gabriel.quarkussocial.domain.repository;

import io.github.gabriel.quarkussocial.domain.model.Follower;
import io.github.gabriel.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean followers(User user, User follower) {

        Map<String, Object> params = Parameters.with("user", user).and("follower", follower).map();
        PanacheQuery<Follower> query = find("user = :user and follower = :follower", params);

        return query.firstResultOptional().isPresent();
    }
}
