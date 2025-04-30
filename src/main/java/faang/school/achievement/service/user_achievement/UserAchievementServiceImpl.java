package faang.school.achievement.service.user_achievement;

import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.achievement.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAchievementServiceImpl implements UserAchievementService {

    private final UserAchievementRepository userAchievementRepository;
    private final AchievementService achievementService;

    @Transactional
    @Override
    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    @Override
    public void giveAchievement(long userId, long achievementId) {
        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievementService.getAchievementById(achievementId))
                .userId(userId)
                .build();

        userAchievementRepository.save(userAchievement);
    }
}