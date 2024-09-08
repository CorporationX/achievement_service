package faang.school.achievement.handler;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.PostEvent;
import faang.school.achievement.handler.opinionLeader.OpinionLeaderAchievementHandler;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static faang.school.achievement.model.Rarity.COMMON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OpinionLeaderHandlerTest {
    private static final String TITLE_OF_ACHIEVEMENT = "OPINION_LEADER";
    private static final long FIRST_ID = 1L;
    private static final long SECOND_ID = 2L;
    private PostEvent postEvent;
    private Achievement achievement;
    private AchievementProgress achievementProgress;
    private AchievementProgressDto progressDto;
    @Mock
    private AchievementCache achievementCache;
    @Mock
    private AchievementService achievementService;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private AchievementMapper mapper;
    @InjectMocks
    private OpinionLeaderAchievementHandler handler;

    @BeforeEach
    void setUp() {
        postEvent = PostEvent.builder()
                .postId(FIRST_ID)
                .postId(SECOND_ID)
                .build();
        achievement = Achievement.builder()
                .id(FIRST_ID)
                .title(TITLE_OF_ACHIEVEMENT)
                .points(SECOND_ID)
                .rarity(COMMON)
                .build();
        AchievementDto achievementDto = AchievementDto.builder()
                .id(achievement.getId())
                .title(achievement.getTitle())
                .points(achievement.getPoints())
                .rarity(achievement.getRarity())
                .build();
        achievementProgress = AchievementProgress.builder()
                .id(FIRST_ID)
                .achievement(achievement)
                .userId(FIRST_ID)
                .currentPoints(FIRST_ID)
                .build();
        progressDto = AchievementProgressDto.builder()
                .id(FIRST_ID)
                .userId(FIRST_ID)
                .achievement(achievementDto)
                .currentPoints(FIRST_ID)
                .build();
    }

    @Test
    void testValidGiveAchievement() {
        //Act
        when(achievementCache.get(any())).thenReturn(achievement);
        when(achievementService.hasAchievement(anyLong(), anyLong())).thenReturn(false);
        when(achievementService.getProgress(anyLong(), anyLong())).thenReturn(progressDto);
        when(mapper.toAchievementProgress(any())).thenReturn(achievementProgress);
        handler.process(postEvent);
        //Assert
        verify(achievementService).giveAchievement(postEvent.getPostId(), achievement);
    }

    @Test
    void testWithoutGiveAchievement() {
        //Arrange
        achievement.setPoints(FIRST_ID);
        //Act
        when(achievementCache.get(any())).thenReturn(achievement);
        when(achievementService.hasAchievement(anyLong(), anyLong())).thenReturn(false);
        when(achievementService.getProgress(anyLong(), anyLong())).thenReturn(progressDto);
        when(mapper.toAchievementProgress(any())).thenReturn(achievementProgress);
        handler.process(postEvent);
        //Assert
        verify(achievementProgressRepository).save(any());
    }

    @Test
    void testUserHasAchievement() {
        //Arrange
        achievement.setPoints(FIRST_ID);
        //Act
        when(achievementCache.get(any())).thenReturn(achievement);
        when(achievementService.hasAchievement(anyLong(), anyLong())).thenReturn(true);
        handler.process(postEvent);
        //Assert
        verify(achievementService, never()).getProgress(anyLong(), any());
    }
}
