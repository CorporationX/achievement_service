package faang.school.achievement.service.achievementprogress;

import faang.school.achievement.exception.AchievementProgressNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test cases of DefaultAchievementProgressServiceTest")
public class DefaultAchievementProgressServiceTest {

    private static final long USER_ID = 1L;
    private static final long ACHIEVEMENT_ID = 1L;
    private static final String ACHIEVEMENT_TITLE = "TEST_ACHIEVEMENT";
    private static final long ACHIEVEMENT_POINTS = 1L;

    @Mock
    private AchievementProgressRepository repository;

    @InjectMocks
    private DefaultAchievementProgressService service;

    private Achievement achievement;
    private AchievementProgress achievementProgress;

    @BeforeEach
    public void setUp() {
        setUpAchievement();
        setUpAchievementProgress();
    }

    @Test
    @DisplayName("createProgressIfNecessary - successfully")
    public void createProgressIfNecessarySuccessfully() {
        service.createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);

        verify(repository, times(1)).createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    @DisplayName("getProgress - not found achievement progress")
    public void testGetProgressWithoutAchievementProgress() {
        when(repository.findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(
                AchievementProgressNotFoundException.class,
                () -> service.getProgress(USER_ID, ACHIEVEMENT_ID)
        );

        assertEquals("Achievement progress not found", exception.getMessage());
    }

    @Test
    @DisplayName("getProgress - successfully")
    public void testGetProgressSuccessfully() {
        when(repository.findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(Optional.of(achievementProgress));

        AchievementProgress actualProgress = service.getProgress(USER_ID, ACHIEVEMENT_ID);

        verify(repository, times(1)).findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
        assertNotNull(actualProgress);
        assertEquals(achievementProgress, actualProgress);
    }

    private void setUpAchievement() {
        achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .title(ACHIEVEMENT_TITLE)
                .points(ACHIEVEMENT_POINTS)
                .build();
    }

    private void setUpAchievementProgress() {
        achievementProgress = AchievementProgress.builder()
                .id(ACHIEVEMENT_ID)
                .userId(USER_ID)
                .achievement(achievement)
                .currentPoints(0L)
                .version(1L)
                .build();
    }
}
