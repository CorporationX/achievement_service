package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AchievementServiceImpl2Test {
    @InjectMocks
    private AchievementServiceImpl2 service;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    private long userId;
    private long achievementId;
    private AchievementProgress achievementProgress;
    private Achievement achievement;

    @BeforeEach
    public void init() {
        userId = 1L;
        achievementId = 1L;
        achievementProgress = new AchievementProgress();
        achievement = new Achievement();
    }

    @Test
    void hasAchievement_whenOk() {
        service.hasAchievement(userId, achievementId);

        Mockito.verify(userAchievementRepository, Mockito.times(1))
                .existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void createProgressIfNecessary_whenOk() {
        service.createProgressIfNecessary(userId, achievementId);

        Mockito.verify(achievementProgressRepository, Mockito.times(1))
                .createProgressIfNecessary(userId, achievementId);
    }

    @Test
    void getProgress_whenOk() {
        Mockito.when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.ofNullable(achievementProgress));

        service.getProgress(userId, achievementId);

        Mockito.verify(achievementProgressRepository, Mockito.times(1))
                .findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void getProgress_whenProgressNotExist() {
        Mockito.when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> service.getProgress(userId, achievementId));

        Mockito.verify(achievementProgressRepository, Mockito.times(1))
                .findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void giveAchievement_whenOk() {
        service.giveAchievement(userId, achievement);

        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setAchievement(achievement);
        userAchievement.setUserId(userId);

        Mockito.verify(userAchievementRepository, Mockito.times(1))
                .save(userAchievement);
    }
}
