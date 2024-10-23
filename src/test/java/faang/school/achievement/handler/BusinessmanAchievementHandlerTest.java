package faang.school.achievement.handler;

import faang.school.achievement.dto.ProjectEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.achievement.AchievementService;
import faang.school.achievement.service.cache.AchievementCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BusinessmanAchievementHandlerTest {
    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private AchievementCacheService achievementCacheService;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private AchievementService achievementService;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @InjectMocks
    private BusinessmanAchievementHandler businessmanAchievementHandler;
    private ProjectEvent event;
    private Achievement businessman;
    private AchievementProgress progress;

    @BeforeEach
    void setUp() {
        event = new ProjectEvent();
        event.setAuthorId(1L);
        businessman = new Achievement();
        businessman.setId(2L);
        progress = new AchievementProgress();
        progress.setId(3L);
        progress.setAchievement(businessman);
        progress.setUserId(1L);
    }

    @Test
    void handleAchievementTest() {
        when(achievementCacheService.getAchievementByTitle("Businessman")).thenReturn(businessman);
        when(achievementService.getProgress(event.getAuthorId(), businessman.getId())).thenReturn(progress);
        when(userAchievementRepository
                .existsByUserIdAndAchievementId(event.getAuthorId(), businessman.getId())).thenReturn(false);

        businessmanAchievementHandler.handleAchievement(event);

        verify(achievementProgressRepository,times(1))
                .createProgressIfNecessary(event.getAuthorId(), businessman.getId());
        verify(achievementProgressRepository,times(1)).save(progress);
        verify(achievementService,times(1)).giveAchievement(event.getAuthorId(), businessman);

    }
}
