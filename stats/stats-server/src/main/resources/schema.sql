CREATE TABLE IF NOT EXISTS stats (
id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
app VARCHAR(512) NOT NULL,
uri VARCHAR(512) NOT NULL,
ip VARCHAR(512) NOT NULL,
time_stamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
);