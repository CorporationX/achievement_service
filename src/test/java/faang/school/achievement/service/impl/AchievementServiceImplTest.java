package faang.school.achievement.service.impl;

import faang.school.achievement.mapper.AchievementMapperImpl;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.AchievementPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AchievementServiceImplTest {

    @InjectMocks
    private AchievementServiceImpl achievementService;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private AchievementRepository achievementRepository;
    @Spy
    private AchievementMapperImpl achievementMapper;
    @Mock
    AchievementPublisher achievementPublisher;
    private final long achievementId = 9L;
    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Test having achievement for user")
    void testHasAchievement() {
        long userId = 1L;
        achievementService.hasAchievement(userId, achievementId);
        Mockito.verify(userAchievementRepository,
                Mockito.times(1)).existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    @DisplayName("Test create progress")
    void testCreateProgressIfNecessary() {
        long userId = 1L;
        achievementService.createProgressIfNecessary(userId, achievementId);
        Mockito.verify(achievementProgressRepository,
                Mockito.times(1)).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    @DisplayName("Test giving achievement")
    void testGiveAchievement() {
        long userId = 1L;
        Achievement achievement = Achievement.builder().id(9L).title("").build();
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();
        Mockito.when(achievementRepository.findById(achievementId)).thenReturn(Optional.ofNullable(achievement));
        achievementService.giveAchievement(userId, achievementId);

        Mockito.verify(userAchievementRepository,
                Mockito.times(1)).save(userAchievement);
        Mockito.verify(achievementPublisher,
                Mockito.times(1)).publishMessage(Mockito.any());
    }

    @Test
    @DisplayName("Test getting achievement")
    void testGetAchievement() {
        Achievement achievement = Achievement.builder().id(9L).title("").build();
        Mockito.when(achievementRepository.findById(achievementId)).thenReturn(Optional.ofNullable(achievement));
        achievementService.getAchievement(achievementId);
        Mockito.verify(achievementRepository,
                Mockito.times(1)).findById(achievementId);
    }
}