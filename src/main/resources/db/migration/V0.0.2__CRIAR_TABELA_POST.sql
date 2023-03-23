CREATE TABLE POSTS
(
    id   bigserial    NOT NULL PRIMARY KEY,
    post_text varchar(150) NOT NULL,
    dateTime  timestamp    NOT NULL,
    user_id bigint not null references USERS(id)
)