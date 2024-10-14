package faang.school.achievement.dto.handler;

import faang.school.achievement.dto.AlbumCreatedEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.CacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LibrarianAchievementHandlerTest {

    @Mock
    private CacheService<String> cacheService;

    @Mock
    private CacheService<Achievement> achievementCacheService;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private LibrarianAchievementHandler librarianAchievementHandler;

    @Test
    void shouldReturnCorrectAchievementName() {
        String achievementName = librarianAchievementHandler.getAchievementName();
        assertEquals("LIBRARIAN", achievementName);
    }

    @Test
    void shouldExtractUserIdFromEvent() {
        AlbumCreatedEvent event = new AlbumCreatedEvent(LocalDateTime.now(), 1L, 2L, "name");
        event.setUserId(123L);

        long userId = librarianAchievementHandler.getUserIdFromEvent(event);

        assertEquals(123L, userId);
    }

    @Test
    void shouldReturnCorrectEventClass() {
        assertEquals(AlbumCreatedEvent.class, librarianAchievementHandler.getEventClass());
    }
}
