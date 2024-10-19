ALTER TABLE achievement
ADD COLUMN threshold bigint NOT NULL DEFAULT 0;

UPDATE achievement
SET threshold = CASE
    WHEN title = 'COLLECTOR' THEN 100
    WHEN title = 'MR PRODUCTIVITY' THEN 1000
    WHEN title = 'EXPERT' THEN 1000
    WHEN title = 'SENSEI' THEN 30
    WHEN title = 'MANAGER' THEN 10
    WHEN title = 'CELEBRITY' THEN 1000000
    WHEN title = 'WRITER' THEN 100
    WHEN title = 'HANDSOME' THEN 1
    ELSE 0
END;