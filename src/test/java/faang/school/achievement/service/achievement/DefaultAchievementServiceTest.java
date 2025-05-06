package faang.school.achievement.service.achievement;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementMapperImpl;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test cases of DefaultAchievementServiceTest")
public class DefaultAchievementServiceTest {

    @Mock
    private AchievementRepository repository;

    @Spy
    private AchievementMapper mapper = new AchievementMapperImpl();

    @InjectMocks
    private DefaultAchievementService service;

    private Achievement firstAchievement;
    private Achievement secondAchievement;

    @BeforeEach
    public void setUp() {
        setUpFirstAchievement();
        setUpSecondAchievement();
    }

    @Test
    @DisplayName("getAchievement - not found achievement")
    public void testGetAchievementWithoutAchievement() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AchievementNotFoundException.class,
                () -> service.getAchievement(1L));

        assertEquals("Achievement with 1 ID not found", exception.getMessage());
    }

    @Test
    @DisplayName("getAchievement - successfully")
    public void testGetAchievementSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(firstAchievement));

        Achievement actualAchievement = service.getAchievement(1L);

        verify(repository, times(1)).findById(1L);
        assertNotNull(actualAchievement);
        assertEquals(firstAchievement, actualAchievement);
    }

    @Test
    @DisplayName("getAchievements - empty achievements DTO list")
    public void testGetAchievementsWithEmptyList() {
        List<Achievement> emptyList = List.of();
        when(repository.findAll()).thenReturn(emptyList);

        List<AchievementDto> actualList = service.getAchievements();

        verify(mapper, times(1)).toDtoList(emptyList);
        verify(repository, times(1)).findAll();
        assertTrue(actualList.isEmpty());
    }

    @Test
    @DisplayName("getAchievements - successfully")
    public void testGetAchievementsSuccessfully() {
        List<Achievement> achievements = List.of(firstAchievement, secondAchievement);
        when(repository.findAll()).thenReturn(achievements);

        List<AchievementDto> resultList = service.getAchievements();

        verify(mapper, times(1)).toDtoList(achievements);
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
    }

    private void setUpFirstAchievement() {
        firstAchievement = Achievement.builder()
                .id(1L)
                .title("First achievement")
                .points(1L)
                .build();
    }

    private void setUpSecondAchievement() {
        secondAchievement = Achievement.builder()
                .id(1L)
                .title("First achievement")
                .points(1L)
                .build();
    }
}
