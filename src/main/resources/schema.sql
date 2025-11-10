CREATE TABLE users (
    id IDENTITY,
    userName VARCHAR(255) NOT NULL
);

CREATE TABLE matches (
    id IDENTITY,
    user1 INT NOT NULL,
    user2 INT NOT NULL,
    user1Hand VARCHAR(255) NOT NULL,
    user2Hand VARCHAR(255),
    isActive BOOLEAN
);

CREATE TABLE matchinfo (
    id IDENTITY,
    user1 INT NOT NULL,
    user2 INT NOT NULL,
    user1Hand VARCHAR(255) NOT NULL,
    isActive BOOLEAN
)
