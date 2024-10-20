package faang.school.achievement.handler;

import faang.school.achievement.dto.PostEventDto;
import faang.school.achievement.model.AchievementTitle;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;


@RequiredArgsConstructor
public class WriterAchievementHandler {
    private final AchievementService achievementService;
    private final long requiredPoints;

    @Async("threadPool")
    public void computeAchievement(PostEventDto postEventDto) {
        Long userId = postEventDto.getAuthorId();
        if (!achievementService.hasAchievement(userId, AchievementTitle.WRITER)) {
            achievementService.updateProgress(userId, AchievementTitle.WRITER, requiredPoints);
        }
    }
}
