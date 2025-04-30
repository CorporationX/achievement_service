package faang.school.achievement.service;

import faang.school.achievement.exception.NotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.achievement.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    @Mock
    private AchievementRepository achievementRepository;

    @InjectMocks
    private AchievementService achievementService;

    private Achievement achievement;
    private long achievementId;

    @BeforeEach
    void setUp() {
        achievementId = 1L;
        achievement = new Achievement();
        achievement.setId(achievementId);
        achievement.setTitle("Test Achievement");
    }

    @Test
    void testGetAchievementByIdSuccess() {
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));

        Achievement result = achievementService.getAchievementById(achievementId);

        assertNotNull(result);
        assertEquals(achievementId, result.getId());
        assertEquals("Test Achievement", result.getTitle());
        verify(achievementRepository).findById(achievementId);
        verifyNoMoreInteractions(achievementRepository);
    }

    @Test
    void testGetAchievementByIdNotFound() {
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            achievementService.getAchievementById(achievementId);
        });

        assertEquals("Achievement was not found", exception.getMessage());
        verify(achievementRepository).findById(achievementId);
        verifyNoMoreInteractions(achievementRepository);
    }
}