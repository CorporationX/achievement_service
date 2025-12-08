package faang.school.achievement.service;

import faang.school.achievement.dto.CommentEventDto;

import java.time.LocalDateTime;

public class DataForTestsObtainAchievements {
    protected static final long POST_ID = 100L;
    protected static final long AUTHOR_ID = 200L;
    protected static final long COMMENT_ID = 300L;
    protected static final long POST_AUTHOR_ID = 400L;
    protected static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    protected static final String ACHIEVEMENT_NAME = "EXPERT";
    protected static final long ACHIEVEMENT_ID = 3L;
    protected static final long POINTS_THRESHOLD = 5L;
    protected static final long HANDLER_EXECUTION_TIME = 5000L;


    public CommentEventDto event = new CommentEventDto(POST_ID, AUTHOR_ID, COMMENT_ID, POST_AUTHOR_ID, LOCAL_DATE_TIME);
}