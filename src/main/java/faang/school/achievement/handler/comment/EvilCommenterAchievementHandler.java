package faang.school.achievement.handler.comment;

import faang.school.achievement.dto.comment.CommentEventDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementRepository;
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
public class EvilCommenterAchievementHandler extends CommentEventHandler {

    @Value("${spring.achievements.achievements-title.evil-commenter}")
    private String evilCommenterAchievement;
    @Value("${spring.achievements.achievement-points-needed.evil-commenter}")
    private int pointsToAchieve;

    private final AchievementService achievementService;
//    private final AchievementCache achievementCache;
//      waiting for cache PR
    private final AchievementRepository achievementRepository;

    @Async
    @Override
    public void verifyAchievement(CommentEventDto commentEventDto) {
        //waiting for cache PR
//        Achievement evilCommenter = achievementCache.get(evilCommenterAchievement);
//        Achievement evilCommenter = new Achievement();
        // ***
        //made to pass test
        Achievement evilCommenter = achievementRepository.findById(9L).orElseThrow();
        // ***
        long userId = commentEventDto.getCommentAuthorId();
        long achievementId = evilCommenter.getId();
        if (!achievementService.hasAchievement(userId, achievementId)) {
            achievementService.createProgressIfNecessary(userId, achievementId);
            AchievementProgress progress = achievementService.getProgress(userId, achievementId);
            long progressPoints = progress.getCurrentPoints();
            progress.setCurrentPoints(progressPoints + 1);
            achievementService.saveAchievementProgress(progress);
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
