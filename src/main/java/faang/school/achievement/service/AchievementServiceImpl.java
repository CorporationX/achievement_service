package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.mapper.AchievementEventMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.AchievementPublisher;
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
    private final AchievementPublisher achievementPublisher;
    private final AchievementEventMapper achievementEventMapper;

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new NoSuchElementException("Progress achievements not found for user ID: " + userId));
    }

    @Transactional
    public void giveAchievement(Achievement achievement, long userId) {
        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setAchievement(achievement);
        userAchievement.setUserId(userId);

        List<UserAchievement> achievementList = achievement.getUserAchievements();
        achievementList.add(userAchievement);
        achievement.setUserAchievements(achievementList);

        achievementEventPublish(achievement, userId);
        userAchievementRepository.save(userAchievement);
    }

    private void achievementEventPublish(Achievement achievement, long userId) {
        AchievementEvent achievementEvent = achievementEventMapper.toDto(achievement);
        achievementEvent.setUserId(userId);
        achievementPublisher.publish(achievementEvent);
    }

}
