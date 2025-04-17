package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.EventType;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @InjectMocks
    private AchievementService achievementService;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Mock
    private AchievementRepository achievementRepository;

    private final long authorId = 1;
    private final long achievementId = 1;
    private final long achievementProgressId = 1;
    private final EventType eventType = EventType.PUBLISHED_POST;
    private final Achievement achievement = Achievement.builder().id(achievementId).goal(10).build();
    private final List<Achievement> achievements = List.of(achievement);
    private final AchievementProgress achievementProgress = AchievementProgress.builder()
            .id(achievementProgressId).currentPoints(9).achievement(achievement).build();
    private final UserAchievement userAchievement = UserAchievement.builder()
            .id(authorId).build();

    @Test
    void getAchievementByEventType_ShouldGet() {
        when(achievementRepository.findByEvent(eventType)).thenReturn(achievements);
        assertEquals(achievements, achievementService.getAchievementByEventType(eventType));
        verify(achievementRepository, times(1)).findByEvent(eventType);
    }

    @Test
    void hasUserAchievement_ShouldHas() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(authorId, achievementId)).thenReturn(true);
        assertTrue(achievementService.hasUserAchievement(authorId, achievementId));
        verify(userAchievementRepository, times(1)).existsByUserIdAndAchievementId(authorId, achievementId);
    }

    @Test
    void incrementAndCheckAchievementProgress_ShouldIncrementAndReturnTrue() {
        when(achievementProgressRepository
                .findForUpdate(authorId, achievementId)).thenReturn(Optional.of(achievementProgress));

        assertTrue(achievementService.incrementAndCheckAchievementProgress(authorId, achievementId));
    }

    @Test
    void incrementAndCheckAchievementProgress_ShouldIncrementAndReturnFalse() {
        achievementProgress.setCurrentPoints(8);
        when(achievementProgressRepository
                .findForUpdate(authorId, achievementId)).thenReturn(Optional.of(achievementProgress));

        assertFalse(achievementService.incrementAndCheckAchievementProgress(authorId, achievementId));
    }


    @Test
    void incrementAndCheckAchievementProgress_ShouldNotIncrementWhenAchievementProgressNotCreate() {
        achievementProgress.setCurrentPoints(8);
        when(achievementProgressRepository
                .findForUpdate(authorId, achievementId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> achievementService.incrementAndCheckAchievementProgress(authorId, achievementId));
    }

    @Test
    void saveAchievementToUser() {
        when(userAchievementRepository.save(any())).thenReturn(userAchievement);

        assertDoesNotThrow(() -> achievementService.saveAchievementToUser(authorId, achievement));
    }
}