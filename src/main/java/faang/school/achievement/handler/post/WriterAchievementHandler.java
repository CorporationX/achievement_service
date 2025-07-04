package faang.school.achievement.handler.post;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import faang.school.achievement.dto.event.PostCreateEventDto;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Async
@RequiredArgsConstructor
public class WriterAchievementHandler implements EventHandler<PostCreateEventDto> {
    private static final String ACHIEVEMENT_TITLE = "WRITER";
    private final AchievementService achievementService;

    @Override
    public void handle(PostCreateEventDto event) {
        log.info("==== log start {}.", event);

        long userId = event.getUserId();

        if (achievementService.hasAchievement(userId, ACHIEVEMENT_TITLE)) {
            return;
        }
        achievementService.incrementProgress(userId, ACHIEVEMENT_TITLE);

        log.info("==== log finish {}.", event);
    }

}
