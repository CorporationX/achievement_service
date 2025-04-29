package faang.school.achievement.service.userachievement;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "app.service.user-achievement",
        havingValue = "default",
        matchIfMissing = true
)
public class DefaultUserAchievementService implements UserAchievementService {

    private final UserAchievementRepository userAchievementRepository;

    @Transactional(readOnly = true)
    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void giveAchievement(long userId, Achievement achievement) {
        String title = achievement.getTitle();
        log.info("Starting giving \"{}\" achievement for user {}...", title, userId);
        userAchievementRepository.save(
                UserAchievement.builder()
                        .achievement(achievement)
                        .userId(userId)
                        .build()
        );
        log.info("Giving successfully \"{}\" achievement for user {}", title, userId);
    }
}
