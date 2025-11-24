package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.MentorshipStartEvent;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SenseyAchievementHandlerTest {

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private SenseyAchievementHandler handler;

    private static final String ACHIEVEMENT_TITLE = "SENSEI";
    private static final long MENTOR_ID = 1L;
    private static final long MENTEE_ID = 2L;

    @Test
    @DisplayName("Should do nothing if user already has achievement")
    void handleUserHasAchievementDoNothing() {
        MentorshipStartEvent event = new MentorshipStartEvent(MENTOR_ID, MENTEE_ID);
        Achievement achievement = new Achievement();
        achievement.setTitle(ACHIEVEMENT_TITLE);

        when(achievementService.getAchievementByTitle(ACHIEVEMENT_TITLE)).thenReturn(achievement);
        when(achievementService.hasAchievement(MENTOR_ID, achievement)).thenReturn(true);

        handler.handle(event);

        verify(achievementService, never()).createProgressIfNecessary(anyLong(), any());
        verify(achievementService, never()).giveAchievement(anyLong(), any());
    }

    @Test
    @DisplayName("Should increment progress but not give achievement if points are not enough")
    void handleNotEnoughPointsIncrementOnly() {
        MentorshipStartEvent event = new MentorshipStartEvent(MENTOR_ID, MENTEE_ID);

        Achievement achievement = new Achievement();
        achievement.setTitle(ACHIEVEMENT_TITLE);
        achievement.setPoints(30); // Нужно 30 очков

        AchievementProgress progress = new AchievementProgress();
        progress.setCurrentPoints(10); // Сейчас будет 10 + 1 = 11

        when(achievementService.getAchievementByTitle(ACHIEVEMENT_TITLE)).thenReturn(achievement);
        when(achievementService.hasAchievement(MENTOR_ID, achievement)).thenReturn(false);
        when(achievementService.createProgressIfNecessary(MENTOR_ID, achievement)).thenReturn(progress);

        handler.handle(event);

        // Проверяем, что прогресс увеличился (метод increment() в твоем коде делает currentPoints++)
        // Здесь мы не мокаем саму сущность progress, она POJO, поэтому points увеличатся реально

        verify(achievementService).createProgressIfNecessary(MENTOR_ID, achievement);
        verify(achievementService, never()).giveAchievement(anyLong(), any());
    }

    @Test
    @DisplayName("Should give achievement if progress reaches required points")
    void handleEnoughPointsGiveAchievement() {
        MentorshipStartEvent event = new MentorshipStartEvent(MENTOR_ID, MENTEE_ID);

        Achievement achievement = new Achievement();
        achievement.setTitle(ACHIEVEMENT_TITLE);
        achievement.setPoints(30); // Нужно 30

        AchievementProgress progress = new AchievementProgress();
        progress.setCurrentPoints(29); // Сейчас 29. После increment() станет 30

        when(achievementService.getAchievementByTitle(ACHIEVEMENT_TITLE)).thenReturn(achievement);
        when(achievementService.hasAchievement(MENTOR_ID, achievement)).thenReturn(false);
        when(achievementService.createProgressIfNecessary(MENTOR_ID, achievement)).thenReturn(progress);

        handler.handle(event);

        // Прогресс стал 30 >= 30, значит должна выдаться ачивка
        verify(achievementService).giveAchievement(MENTOR_ID, achievement);
    }
}