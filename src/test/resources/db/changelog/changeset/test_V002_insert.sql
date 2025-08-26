INSERT INTO users (username)
VALUES
    ('JohnDoe'),
    ('JaneSmith'),
    ('MichaelJohnson');

INSERT INTO achievement (title, description, rarity, points, created_at, updated_at)
VALUES
    ('COLLECTOR', 'For 100 goals', 3, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('MR PRODUCTIVITY', 'For 1000 finished tasks', 4, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('EXPERT', 'For 1000 comments', 1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('SENSEI', 'For 30 mentees', 4, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('MANAGER', 'For 10 teams', 2, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('CELEBRITY', 'For 1 000 000 subscribers', 4, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('WRITER', 'For 100 posts published', 2, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('HANDSOME', 'For uploaded profile photo', 0, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO user_achievement(user_id, achievement_id)
VALUES(1, 3);

INSERT INTO user_achievement_progress(user_id, achievement_id, current_points)
VALUES
    (1, 3, 5),
    (3, 3, 1);