package faang.school.achievement.dto.handler2;

import faang.school.achievement.dto.PostEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OpinionLeaderAchievementHandlerTest {
    private static final String ACHIEVEMENT_NAME = "OPINION_LEADER";

    @InjectMocks
    private OpinionLeaderAchievementHandler opinionLeaderAchievementHandler;

    @Mock
    private AchievementService achievementService;

    @Mock
    private CacheService<Achievement> cacheService;

    private PostEvent postEvent;
    private Achievement achievement;
    private AchievementProgress achievementProgress;

    @BeforeEach
    public void init() {
        postEvent = new PostEvent(1L, 1L);

        achievement = new Achievement();
        achievement.setPoints(1000L);
        achievement.setId(1L);
        achievementProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(1L);

        Mockito.lenient().when(cacheService.get(ACHIEVEMENT_NAME, Achievement.class))
                .thenReturn(achievement);
    }


    @Test
    void handle_whenUserAlreadyGotAchievement() {
        Mockito.when(achievementService.hasAchievement(postEvent.getAuthorId(), achievement.getId()))
                .thenReturn(true);

        opinionLeaderAchievementHandler.handle(postEvent);

        Mockito.verify(cacheService, Mockito.times(1))
                .get(ACHIEVEMENT_NAME, Achievement.class);

        Mockito.verify(achievementService, Mockito.times(1))
                .hasAchievement(postEvent.getAuthorId(), achievement.getId());

        Mockito.verify(achievementService, Mockito.never())
                .createProgressIfNecessary(postEvent.getAuthorId(), achievement.getId());

        Mockito.verify(achievementService, Mockito.never())
                .getProgress(postEvent.getAuthorId(), achievement.getId());

        Mockito.verify(achievementService, Mockito.never())
                .giveAchievement(postEvent.getAuthorId(), achievement);
    }

    @Test
    void handle_whenNotEnoughPoints() {
        Mockito.lenient().when(achievementService.getProgress(postEvent.getAuthorId(), achievement.getId()))
                .thenReturn(achievementProgress);
        Mockito.when(achievementService.hasAchievement(postEvent.getAuthorId(), achievement.getId()))
                .thenReturn(false);

        opinionLeaderAchievementHandler.handle(postEvent);

        Mockito.verify(cacheService, Mockito.times(1))
                .get(ACHIEVEMENT_NAME, Achievement.class);

        Mockito.verify(achievementService, Mockito.times(1))
                .hasAchievement(postEvent.getAuthorId(), achievement.getId());

        Mockito.verify(achievementService, Mockito.times(1))
                .createProgressIfNecessary(postEvent.getAuthorId(), achievement.getId());

        Mockito.verify(achievementService, Mockito.times(1))
                .getProgress(postEvent.getAuthorId(), achievement.getId());

        Mockito.verify(achievementService, Mockito.never())
                .giveAchievement(postEvent.getAuthorId(), achievement);
    }

    @Test
    void handle_whenEnoughPoints() {
        achievementProgress.setCurrentPoints(999L);
        Mockito.lenient().when(achievementService.getProgress(postEvent.getAuthorId(), achievement.getId()))
                .thenReturn(achievementProgress);
        Mockito.when(achievementService.hasAchievement(postEvent.getAuthorId(), achievement.getId()))
                .thenReturn(false);

        opinionLeaderAchievementHandler.handle(postEvent);

        Mockito.verify(cacheService, Mockito.times(1))
                .get(ACHIEVEMENT_NAME, Achievement.class);

        Mockito.verify(achievementService, Mockito.times(1))
                .hasAchievement(postEvent.getAuthorId(), achievement.getId());

        Mockito.verify(achievementService, Mockito.times(1))
                .createProgressIfNecessary(postEvent.getAuthorId(), achievement.getId());

        Mockito.verify(achievementService, Mockito.times(1))
                .getProgress(postEvent.getAuthorId(), achievement.getId());

        Mockito.verify(achievementService, Mockito.times(1))
                .giveAchievement(postEvent.getAuthorId(), achievement);
    }
}
