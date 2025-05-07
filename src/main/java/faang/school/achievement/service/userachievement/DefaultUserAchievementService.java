package faang.school.achievement.service.userachievement;

import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.achievement.AchievementService;
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
    private final AchievementService achievementService;

    @Transactional(readOnly = true)
    @Override
    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    @Override
    public void assignAchievementToUser(long userId, long achievementId) { //TODO возможно стоит объединить методы
        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievementService.getAchievementById(achievementId))
                .userId(userId)
                .build();

        userAchievementRepository.save(userAchievement);
    }

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