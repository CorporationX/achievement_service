package faang.school.achievement.service.userachievement;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test cases of DefaultUserAchievementServiceTest")
public class DefaultUserAchievementServiceTest {

    private static final long USER_ID = 1L;
    private static final long ACHIEVEMENT_ID = 1L;

    @Mock
    private UserAchievementRepository repository;

    @InjectMocks
    private DefaultUserAchievementService service;

    @Captor
    private ArgumentCaptor<UserAchievement> captor;

    @Test
    @DisplayName("hasAchievement - user without achievement")
    public void testHasAchievementWithoutAchievement() {
        boolean result = service.hasAchievement(USER_ID, ACHIEVEMENT_ID);

        assertFalse(result);
    }

    @Test
    @DisplayName("hasAchievement - successfully")
    public void testHasAchievementSuccessfully() {
        when(repository.existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(true);

        boolean result = service.hasAchievement(USER_ID, ACHIEVEMENT_ID);

        assertTrue(result);
    }

    @Test
    @DisplayName("giveAchievement - successfully")
    public void testGiveAchievementSuccessfully() {
        Achievement achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .title("Test achievement")
                .points(1L)
                .build();

        UserAchievement expectedUserAchievement = UserAchievement.builder()
                .achievement(achievement)
                .userId(USER_ID)
                .build();

        service.giveAchievement(USER_ID, achievement);

        verify(repository, times(1)).save(captor.capture());
        UserAchievement actualUserAchievement = captor.getValue();
        assertNotNull(actualUserAchievement);
        assertEquals(expectedUserAchievement, actualUserAchievement);
    }
}
