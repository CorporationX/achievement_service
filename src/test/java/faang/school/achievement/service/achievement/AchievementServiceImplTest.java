package faang.school.achievement.service.achievement;

import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.AchievementPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.CacheService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceImplTest {

    private final long userId = 1L;
    private final long achievementId = 2L;
    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Mock
    private UserAchievementRepository achievementUserRepository;

    @Mock
    private CacheService<Achievement> cacheService;

    @Mock
    private AchievementPublisher achievementPublisher;

    @InjectMocks
    private AchievementServiceImpl achievementService;

    @Captor
    private ArgumentCaptor<UserAchievement> userAchievementCaptor;

    @Captor
    private ArgumentCaptor<AchievementEvent> achievementEventCaptor;

    @Test
    void initAchievements_shouldCacheAllAchievements() {
        Achievement first = Achievement.builder().title("first").build();
        Achievement second = Achievement.builder().title("second").build();
        List<Achievement> achievements = List.of(first, second);
        when(achievementRepository.findAll()).thenReturn(achievements);

        achievementService.initAchievements();

        verify(cacheService).put(first.getTitle(), first);
        verify(cacheService).put(second.getTitle(), second);
    }

    @Test
    void hasAchievement_shouldReturnTrueIfAchievementExists() {
        when(achievementUserRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(true);

        boolean result = achievementService.hasAchievement(userId, achievementId);

        assertTrue(result);
    }

    @Test
    void hasAchievement_shouldReturnFalseIfAchievementDoesNotExist() {
        when(achievementUserRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(false);

        boolean result = achievementService.hasAchievement(userId, achievementId);

        assertFalse(result);
    }

    @Test
    void getProgress_shouldReturnProgressWhenFound() {
        AchievementProgress expectedProgress = new AchievementProgress();
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.of(expectedProgress));

        AchievementProgress result = achievementService.getProgress(userId, achievementId);

        assertEquals(expectedProgress, result);
    }

    @Test
    void getProgress_shouldThrowExceptionWhenNotFound() {
        String message = "Achievement %d progress not found".formatted(achievementId);
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> achievementService.getProgress(userId, achievementId));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void createProgressIfNecessary_shouldCallRepositoryMethod() {
        achievementService.createProgressIfNecessary(userId, achievementId);

        verify(achievementProgressRepository).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    void giveAchievement_shouldSaveUserAchievement() {
        Achievement achievement = Achievement.builder()
                .id(achievementId)
                .build();
        UserAchievement correctUserAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();

        achievementService.giveAchievement(userId, achievement);

        verify(achievementUserRepository).save(userAchievementCaptor.capture());
        assertEquals(correctUserAchievement, userAchievementCaptor.getValue());
        verify(achievementPublisher).publish(achievementEventCaptor.capture());

        AchievementEvent capturedEvent = achievementEventCaptor.getValue();
        assertEquals(userId, capturedEvent.getUserId());
        assertEquals(achievementId, capturedEvent.getAchievementId());
    }
}
