ALTER TABLE achievement
ADD COLUMN event VARCHAR(128),
ADD COLUMN goal BIGINT;

UPDATE achievement
SET event = 'GOAL', goal = 100
WHERE title = 'COLLECTOR';

UPDATE achievement
SET event = 'FINISHED_TASK', goal = 1000
WHERE title = 'MR PRODUCTIVITY';

UPDATE achievement
SET event = 'COMMENT', goal = 1000
WHERE title = 'EXPERT';

UPDATE achievement
SET event = 'MENTEE', goal = 30
WHERE title = 'SENSEI';

UPDATE achievement
SET event = 'TEAM', goal = 10
WHERE title = 'MANAGER';

UPDATE achievement
SET event = 'SUBSCRIBER', goal = 1000000
WHERE title = 'CELEBRITY';

UPDATE achievement
SET event = 'PUBLISHED_POST', goal = 100
WHERE title = 'WRITER';

UPDATE achievement
SET event = 'UPLOADED_PROFILE_PHOTO', goal = 1
WHERE title = 'HANDSOME';

ALTER TABLE achievement
ALTER COLUMN event SET NOT NULL,
ALTER COLUMN goal SET NOT NULL;

ALTER TABLE achievement
ADD CONSTRAINT event_unique UNIQUE (event);