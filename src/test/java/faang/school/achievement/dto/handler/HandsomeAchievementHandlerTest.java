package faang.school.achievement.dto.handler;

import faang.school.achievement.dto.ProfilePicEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.CacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HandsomeAchievementHandlerTest {

    @Mock
    private CacheService<String> cacheService;

    @Mock
    private CacheService<Achievement> achievementCacheService;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private HandsomeAchievementHandler handsomeAchievementHandler;

    @Test
    void shouldReturnCorrectAchievementName() {
        String achievementName = handsomeAchievementHandler.getAchievementName();

        assertEquals("HANDSOME", achievementName);
    }

    @Test
    void shouldExtractUserIdFromEvent() {
        ProfilePicEvent event = new ProfilePicEvent(LocalDateTime.now(), 123L, "key");

        long userId = handsomeAchievementHandler.getUserIdFromEvent(event);

        assertEquals(123L, userId);
    }

    @Test
    void shouldReturnCorrectEventClass() {
        assertEquals(ProfilePicEvent.class, handsomeAchievementHandler.getEventClass());
    }

    @Test
    void shouldReturnCorrectHandlerClass() {
        assertEquals(HandsomeAchievementHandler.class, handsomeAchievementHandler.getHandlerClass());
    }
}
