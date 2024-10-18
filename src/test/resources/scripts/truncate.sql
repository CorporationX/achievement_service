delete from user_achievement;
SELECT setval((SELECT pg_get_serial_sequence('user_achievement', 'id')), 1, false);

delete from user_achievement_progress;
SELECT setval((SELECT pg_get_serial_sequence('user_achievement_progress', 'id')), 1, false);