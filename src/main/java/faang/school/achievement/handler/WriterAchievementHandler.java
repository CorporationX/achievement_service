package faang.school.achievement.handler;

import faang.school.achievement.dto.PostEventDto;
import faang.school.achievement.model.AchievementTitle;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
public class WriterAchievementHandler {
    private final AchievementService achievementService;
    private final long requiredPoints;

    @Async("threadPool")
    @Transactional
    public void computeAchievement(PostEventDto postEventDto) {
        Long userId = postEventDto.getAuthorId();
        if (!achievementService.hasAchievement(userId, AchievementTitle.WRITER)) {
            achievementService.updateProgress(userId, AchievementTitle.WRITER, requiredPoints);
        }
    }
}
