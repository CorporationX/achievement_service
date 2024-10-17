package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService{

    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new NoSuchElementException("Прогресс достижения не найдено для пользователя с ID: " + userId));
    }

    @Transactional
    public void giveAchievement(Achievement achievement, long userId) {
        List<UserAchievement> achievementList = userAchievementRepository.findByUserId(userId);
        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setAchievement(achievement);
        userAchievement.setUserId(userId);
        achievementList.add(userAchievement); // не уверен что он будет перезаписываться у сущности, подскажите плз
        userAchievementRepository.save(userAchievement);
    }


}
