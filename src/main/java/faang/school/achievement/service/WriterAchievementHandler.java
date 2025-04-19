package faang.school.achievement.service;

import org.springframework.stereotype.Component;

@Component
public class WriterAchievementHandler extends AbstractAchievementHandler {
    private static final String WRITER_TITLE_ACHIEVEMENT = "Writer";

    public WriterAchievementHandler(
            AchievementService achievementService
    ) {
        super(achievementService, WRITER_TITLE_ACHIEVEMENT);
    }
}
