package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @InjectMocks
    private AchievementServiceImpl achievementService;

    private final Long USER_ID = 1L;
    private final Long ACHIEVEMENT_ID = 100L;

    @Test
    public void operationAchievement_WhenAchievementNotExists() {
        Achievement achievement = createTestAchievement();
        String achievementTitle = "test";
        when(achievementRepository.findByTitle(achievementTitle)).thenReturn(achievement);
        when(userAchievementRepository.existsByUserIdAndAchievementId(USER_ID, achievement.getId()))
                .thenReturn(false);
        when(achievementProgressRepository.findByUserIdAndAchievementId(USER_ID, achievement.getId()))
                .thenReturn(Optional.of(createTestProgress()));

        achievementService.operationAchievement(USER_ID, achievementTitle);

        verify(achievementRepository).findByTitle(achievementTitle);
        verify(userAchievementRepository).existsByUserIdAndAchievementId(USER_ID, achievement.getId());
        verify(achievementProgressRepository).createProgressIfNecessary(USER_ID, achievement.getId());
        verify(userAchievementRepository).save(any(UserAchievement.class));
    }

    @Test
    public void operationAchievement_WhenAchievementExists() {
        Achievement achievement = createTestAchievement();
        String achievementTitle = "test";
        when(achievementRepository.findByTitle(achievementTitle)).thenReturn(achievement);
        when(userAchievementRepository.existsByUserIdAndAchievementId(USER_ID, achievement.getId()))
                .thenReturn(true);

        achievementService.operationAchievement(USER_ID, achievementTitle);

        verify(userAchievementRepository, times(0)).save(any(UserAchievement.class));

    }

    @Test
    public void testGiveAchievement() {
        Achievement achievement = createTestAchievement();
        UserAchievement savedUserAchievement = createTestUserAchievement();

        when(userAchievementRepository.save(any(UserAchievement.class)))
                .thenReturn(savedUserAchievement);

        achievementService.giveAchievement(USER_ID, achievement);

        verify(userAchievementRepository).save(argThat(ua ->
                USER_ID.equals(ua.getUserId()) &&
                        ua.getAchievement().equals(achievement)
        ));
    }

    @Test
    public void testGetAchievementByTitleNotFound() {
        when(achievementRepository.findByTitle("test"))
                .thenReturn(null);

        Achievement result = achievementService.getAchievementByTitle("test");

        assertNull(result);
        verify(achievementRepository).findByTitle("test");
    }

    private Achievement createTestAchievement() {
        return Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .title("ACHIEVEMENT_TITLE")
                .points(10L)
                .build();
    }

    private UserAchievement createTestUserAchievement() {
        return UserAchievement.builder()
                .id(1L)
                .userId(USER_ID)
                .achievement(createTestAchievement())
                .build();
    }

    private AchievementProgress createTestProgress() {
        return AchievementProgress.builder()
                .id(1L)
                .userId(USER_ID)
                .achievement(createTestAchievement())
                .currentPoints(10L)
                .build();
    }
}
