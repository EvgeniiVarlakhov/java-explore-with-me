CREATE TABLE IF NOT EXISTS users
(
    id        INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_name VARCHAR(512)                             NOT NULL,
    email     VARCHAR(512)                             NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    cat_name VARCHAR(512)                             NOT NULL,
    CONSTRAINT cat_pk PRIMARY KEY (id),
    CONSTRAINT uq_cat_name UNIQUE (cat_name)
);

CREATE TABLE IF NOT EXISTS events
(
    id                INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation        VARCHAR(2000),
    cat_id            INTEGER                                  NOT NULL,
    created_on        TIMESTAMP WITHOUT TIME ZONE              NOT NULL,
    description       VARCHAR(7000),
    event_date        TIMESTAMP WITHOUT TIME ZONE              NOT NULL,
    initiator_id      INTEGER                                  NOT NULL,
    loc_lat           DOUBLE PRECISION,
    loc_lon           DOUBLE PRECISION,
    paid              BOOLEAN,
    participant_limit INTEGER                                  NOT NULL,
    published_on      TIMESTAMP WITHOUT TIME ZONE,
    req_moderation    BOOLEAN,
    state             VARCHAR(120),
    title             VARCHAR(120),
    CONSTRAINT event_pk PRIMARY KEY (id),
    CONSTRAINT fk_user_id FOREIGN KEY (initiator_id) references users (id),
    CONSTRAINT fk_cat_id FOREIGN KEY (cat_id) references categories (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN,
    title  VARCHAR(120),
    CONSTRAINT comp_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    comp_id  INTEGER NOT NULL,
    event_id INTEGER NOT NULL,
    CONSTRAINT comp_event_pk PRIMARY KEY (comp_id, event_id),
    CONSTRAINT c_fk FOREIGN KEY (comp_id) REFERENCES compilations (id),
    CONSTRAINT e_fk FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id     INTEGER                                  NOT NULL,
    requester_id INTEGER                                  NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE              NOT NULL,
    status       VARCHAR(120),
    CONSTRAINT req_pk PRIMARY KEY (id),
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) references events (id),
    CONSTRAINT fk_req_id FOREIGN KEY (requester_id) references users (id),
    CONSTRAINT unique_req UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text_comment varchar(2000)                             NOT NULL,
    event_id     INTEGER                                  NOT NULL,
    author_id    INTEGER                                  NOT NULL,
    created_time timestamp without time zone              NOT NULL,
    CONSTRAINT pk_comments_id PRIMARY KEY (id),
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES users (id)
);