ALTER TABLE achievement
ADD COLUMN required_points INTEGER NULL;

UPDATE achievement
SET required_points = CASE
    WHEN title = 'COLLECTOR' THEN 100
    WHEN title = 'MR PRODUCTIVITY' THEN 1000
    WHEN title = 'EXPERT' THEN 1000
    WHEN title = 'SENSEI' THEN 30
    WHEN title = 'MANAGER' THEN 10
    WHEN title = 'CELEBRITY' THEN 1000000
    WHEN title = 'WRITER' THEN 100
    WHEN title = 'HANDSOME' THEN 1
END;

ALTER TABLE achievement
ALTER COLUMN required_points SET NOT NULL,
ADD CONSTRAINT required_points_range CHECK (
    required_points >= 1
);

