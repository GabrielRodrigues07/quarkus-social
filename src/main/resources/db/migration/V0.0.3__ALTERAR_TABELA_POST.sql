ALTER TABLE posts
    ADD CONSTRAINT post_use FOREIGN KEY (user_id) REFERENCES users (id);