package faang.school.achievement.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementType;
import faang.school.achievement.dto.event.CommentEvent;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
class ExpertAchievementHandlerTest {
    @InjectMocks
    private ExpertAchievementHandler handler;
    @Mock
    private AchievementService achievementService;
    @Mock
    private CacheService cacheService;
    @Spy
    private ObjectMapper objectMapper;
    private CommentEvent event;

    private static final long USER_ID = 1L;
    private static final long ACHIEVEMENT_ID = 3L;
    private static final long PROGRESS_ID = 2L;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        event = createCommentEvent();
        String json = toJson(createAchievementDto());
        when(cacheService.getAchievement(AchievementType.EXPERT.name())).thenReturn(json);
        when(achievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID)).thenReturn(false);
    }

    @Test
    void positive_handleAndCreateAchieve() {
        AchievementProgress progress = createAchievementProgress(4);
        when(achievementService.getProgress(USER_ID, ACHIEVEMENT_ID)).thenReturn(progress);
        progress.increment();
        when(achievementService.incrementAndGetPointsById(progress.getId())).thenReturn(progress.getCurrentPoints());

        handler.handle(event);

        verify(achievementService, times(1)).giveAchievement(ACHIEVEMENT_ID, USER_ID);
    }

    @Test
    void positive_handleAndUpdateProgress() {
        when(achievementService.getProgress(USER_ID, ACHIEVEMENT_ID)).thenReturn(createAchievementProgress(3));

        handler.handle(event);

        verify(achievementService, never()).giveAchievement(ACHIEVEMENT_ID, USER_ID);
    }

    // -------------------

    private CommentEvent createCommentEvent() {
        return CommentEvent.builder()
                .id(1)
                .postAuthorId(2)
                .commentAuthorId(USER_ID)
                .postId(4)
                .content("comment content")
                .build();
    }

    private AchievementDto createAchievementDto() {
        return AchievementDto.builder()
                .id(ACHIEVEMENT_ID)
                .title(AchievementType.EXPERT)
                .points(5)
                .build();
    }

    private AchievementProgress createAchievementProgress(long currentPoints) {
        return AchievementProgress.builder()
                .id(PROGRESS_ID)
                .currentPoints(currentPoints)
                .build();
    }

    private <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}