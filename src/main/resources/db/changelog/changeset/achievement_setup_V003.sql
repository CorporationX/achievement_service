INSERT INTO achievement (title, description, rarity, points, created_at, updated_at)
VALUES
    ('NOVICE TRACKER', 'For 100 requested yourself hashtag', 1, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TREND HUNTER', 'For 10000 requested yourself hashtag', 3, 10000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('SEARCH GURU', 'For 100000 requested yourself hashtag', 4, 100000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (title) DO NOTHING;
