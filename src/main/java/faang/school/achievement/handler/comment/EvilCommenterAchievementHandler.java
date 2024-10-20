package faang.school.achievement.handler.comment;

import faang.school.achievement.config.cache.AchievementCache;
import faang.school.achievement.dto.comment.NewCommentEventDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.achievement.AchievementService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class EvilCommenterAchievementHandler extends NewCommentEventHandler {

    @Value("${achievements.achievements-title.evil-commenter}")
    private String evilCommenterAchievement;
    @Value("${achievements.achievement-points-needed.evil-commenter}")
    private int pointsToAchieve;

    private final AchievementService achievementService;
    private final AchievementCache achievementCache;

    @Async
    @Override
    public void verifyAchievement(NewCommentEventDto newCommentEventDto) {
        Achievement evilCommenter = achievementCache.getAchievement(evilCommenterAchievement);
        long userId = newCommentEventDto.getCommentAuthorId();
        long achievementId = evilCommenter.getId();

        if (!achievementService.hasAchievement(userId, achievementId)) {
            AchievementProgress progress = achievementService
                    .proceedAchievementProgress(userId,achievementId);
            if (progress.getCurrentPoints() == pointsToAchieve) {
                UserAchievement userAchievement = UserAchievement.builder()
                        .achievement(evilCommenter)
                        .userId(userId)
                        .build();
                log.debug("Giving achievement {}! To user {}!", evilCommenterAchievement, userId);
                achievementService.giveAchievement(userAchievement);
            }
        }
    }
}
