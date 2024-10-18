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
    updated_at timestamptz DEFAULT current_timestamp
);

CREATE UNIQUE INDEX user_achievement_idx ON user_achievement (user_id, achievement_id);

CREATE TABLE user_achievement_progress (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id bigint NOT NULL,
    achievement_id bigint NOT NULL,
    current_points bigint NOT NULL,
    version bigint NOT NULL DEFAULT 0,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp
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
    ('HANDSOME', 'For uploaded profile photo', 0, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)