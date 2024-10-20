package faang.school.achievement.handler.comment;

import faang.school.achievement.config.cache.AchievementCache;
import faang.school.achievement.dto.comment.NewCommentEventDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.achievement.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EvilCommenterAchievementHandlerTest {

    private static final long ACHIEVEMENT_ID_NINE = 9L;
    private static final long ACHIEVEMENT_POINTS = 4L;
    private static final String TITLE = "EVIL COMMENTER";
    private static final Rarity UNCOMMON = Rarity.UNCOMMON;
    private static final long AUTHOR_ID_ONE = 1L;
    private static final String DESCRIPTION = "For 100 comments";
    private static final int CURRENT_POINTS = 100;
    private static final LocalDateTime TIME = LocalDateTime.of(2024, 10, 10, 10, 10);
    @Mock
    private AchievementService achievementService;
    @Mock
    private AchievementCache achievementCache;
    private Achievement achievement;
    private NewCommentEventDto eventDto;
    private AchievementProgress achievementProgress;
    private UserAchievement userAchievement;


    @BeforeEach
    void setUp() {
        achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID_NINE)
                .title(TITLE)
                .description(DESCRIPTION)
                .rarity(UNCOMMON)
                .points(ACHIEVEMENT_POINTS)
                .createdAt(TIME)
                .updatedAt(TIME)
                .build();
        eventDto = NewCommentEventDto.builder()
                .commentAuthorId(AUTHOR_ID_ONE)
                .build();
        userAchievement = UserAchievement.builder()
                .achievement(achievement)
                .userId(AUTHOR_ID_ONE)
                .build();
        achievementProgress = AchievementProgress.builder()
                .currentPoints(CURRENT_POINTS)
                .build();
    }

    @Test
    @DisplayName("When newCommentEventDto passed then verify it")
    public void whenCommentDtoPassedThenVerifyIt() {
        EvilCommenterAchievementHandler handler =
                new EvilCommenterAchievementHandler(achievementService, achievementCache);
        ReflectionTestUtils.setField(handler, "pointsToAchieve", CURRENT_POINTS);
        ReflectionTestUtils.setField(handler, "evilCommenterAchievement", TITLE);
        when(achievementCache.getAchievement(TITLE)).thenReturn(achievement);
        when(achievementService.hasAchievement(AUTHOR_ID_ONE, ACHIEVEMENT_ID_NINE)).thenReturn(false);
        when(achievementService.proceedAchievementProgress(AUTHOR_ID_ONE, ACHIEVEMENT_ID_NINE))
                .thenReturn(achievementProgress);

        handler.verifyAchievement(eventDto);
        verify(achievementService).giveAchievement(userAchievement);
    }
}
