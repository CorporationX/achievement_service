package faang.school.achievement.messaging.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.SkillAcquiredEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementType;
import faang.school.achievement.service.AchievementEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WhoeverAchievementHandlerTest {

    @Mock
    private AchievementEventService achievementEventService;

    @Mock
    private AchievementCache achievementCache;

    @Mock
    private SkillAcquiredEvent event;

    @Mock
    private Achievement achievement;

    private WhoeverAchievementHandler handler;
    String title;

    @BeforeEach
    void setUp() {
        title = AchievementType.SKILL_KEEPER.getTitle();
        handler = new WhoeverAchievementHandler(achievementEventService, achievementCache);
    }

    @Test
    void testHandleEvent() {
        when(achievementCache.get(title)).thenReturn(achievement);

        handler.handleEvent(event);

        verify(achievementCache).get(title);
        verify(achievementEventService).processAchievementAcquired(event, achievement);
    }
}
