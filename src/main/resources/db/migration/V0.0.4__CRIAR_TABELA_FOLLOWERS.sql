CREATE TABLE FOLLOWERS
(
    id          bigserial NOT NULL PRIMARY KEY,
    user_id     bigint    NOT NULL REFERENCES USERS (id),
    follower_id bigint    NOT NULL REFERENCES USERS (id)
);

ALTER TABLE FOLLOWERS ADD CONSTRAINT fol_use FOREIGN KEY(user_id) references USERS(id);

ALTER TABLE FOLLOWERS ADD CONSTRAINT fol_use FOREIGN KEY(follower_id) references USERS(id);