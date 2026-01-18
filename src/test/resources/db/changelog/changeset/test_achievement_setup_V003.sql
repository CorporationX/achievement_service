-- TEST ONLY
-- creating and inserting a test-achievement for abstract achievement handling

INSERT INTO achievement (title, description, rarity, points, created_at, updated_at)
VALUES ('TEST', 'tests, not real', 4, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)