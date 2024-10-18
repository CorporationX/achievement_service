INSERT INTO achievement(title, description, rarity, points, created_at, updated_at)
VALUES ('OPINION_LEADER', 'For 1000 posts', 3, 1000, now(), now())
    ON CONFLICT (title)
DO NOTHING 
