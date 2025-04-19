package faang.school.achievement.service;

import faang.school.achievement.dto.PostEvent;
import org.springframework.stereotype.Component;

@Component
public class WriterAchievementHandler extends AbstractAchievementHandler<PostEvent> {
    private static final String WRITER_TITLE_ACHIEVEMENT = "Writer";

    public WriterAchievementHandler(
            AchievementService achievementService
    ) {
        super(achievementService, WRITER_TITLE_ACHIEVEMENT);
    }

    @Override
    protected Long getUserId(PostEvent event) {
        return event.getAuthorId();
    }
}
