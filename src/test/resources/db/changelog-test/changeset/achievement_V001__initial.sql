CREATE TABLE country (
                         id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
                         title varchar(64) UNIQUE NOT NULL
);

CREATE TABLE users (
                       id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
                       username varchar(64) UNIQUE NOT NULL,
                       password varchar(128) NOT NULL,
                       email varchar(64) UNIQUE NOT NULL,
                       phone varchar(32) UNIQUE,
                       about_me varchar(4096),
                       active boolean DEFAULT true NOT NULL,
                       city varchar(64),
                       country_id bigint NOT NULL,
                       experience int,
                       created_at timestamptz DEFAULT current_timestamp,
                       updated_at timestamptz DEFAULT current_timestamp,

                       CONSTRAINT fk_country_id FOREIGN KEY (country_id) REFERENCES country (id)
);

CREATE TABLE achievement (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    title VARCHAR(128) NOT NULL UNIQUE,
    description VARCHAR(1024) NOT NULL UNIQUE,
    rarity smallint NOT NULL,
    points bigint NOT NULL,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp
);

CREATE TABLE user_achievement (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id bigint NOT NULL,
    achievement_id bigint NOT NULL,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp,

    CONSTRAINT fk_user_achievement_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE UNIQUE INDEX user_achievement_idx ON user_achievement (user_id, achievement_id);

CREATE TABLE user_achievement_progress (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id bigint NOT NULL,
    achievement_id bigint NOT NULL,
    current_points bigint NOT NULL,
    version bigint NOT NULL DEFAULT 0,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp,

    CONSTRAINT fk_user_achievement_progress_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE UNIQUE INDEX user_achievement_progress_idx ON user_achievement_progress (user_id, achievement_id);

INSERT INTO achievement (title, description, rarity, points, created_at, updated_at)
VALUES
    ('COLLECTOR', 'For 100 goals', 3, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('MR PRODUCTIVITY', 'For 1000 finished tasks', 4, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('EXPERT', 'For 1000 comments', 1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('SENSEI', 'For 30 mentees', 4, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('MANAGER', 'For 10 teams', 2, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('CELEBRITY', 'For 1 000 000 subscribers', 4, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('WRITER', 'For 100 posts published', 2, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('HANDSOME', 'For uploaded profile photo', 0, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('BUSINESSMAN', 'For 5 created projects', 0, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO country (title)
VALUES
    ('USA');

INSERT INTO users (username, password, email, country_id)
VALUES
    ('jane_doe', 'hashed_password_456', 'jane.doe@example.com', 1);