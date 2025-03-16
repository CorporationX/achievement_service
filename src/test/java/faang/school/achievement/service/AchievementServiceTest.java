package faang.school.achievement.service;

import faang.school.achievement.dto.event.GiveAchievementEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.impl.AchievementServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {
  @Mock
  private AchievementRepository achievementRepository;

  @Mock
  private UserAchievementRepository userAchievementRepository;

  @Mock
  private AchievementProgressRepository achievementProgressRepository;

  @Mock
  private ApplicationEventPublisher eventPublisher;

  @InjectMocks
  private AchievementServiceImpl achievementService;

  private static final Long TEST_USER_ID = 1L;
  private Achievement testAchievement;
  private AchievementProgress testProgress;

  @BeforeEach
  void setUp() {
    testAchievement = Achievement.builder()
        .id(1L)
        .title("TeamCreator")
        .points(10)
        .build();

    testProgress = AchievementProgress.builder()
        .id(1L)
        .userId(TEST_USER_ID)
        .achievement(testAchievement)
        .currentPoints(5)
        .build();
  }

  @Test
  void hasAchievement_ShouldReturnTrue_WhenUserHasAchievement() {
    when(userAchievementRepository.existsByUserIdAndAchievementId(TEST_USER_ID, testAchievement.getId())).thenReturn(true);

    boolean result = achievementService.hasAchievement(TEST_USER_ID, testAchievement);

    assertTrue(result);
    verify(userAchievementRepository, times(1)).existsByUserIdAndAchievementId(TEST_USER_ID, testAchievement.getId());
  }

  @Test
  void hasAchievement_ShouldReturnFalse_WhenUserDoesNotHaveAchievement() {
    when(userAchievementRepository.existsByUserIdAndAchievementId(TEST_USER_ID, testAchievement.getId())).thenReturn(false);

    boolean result = achievementService.hasAchievement(TEST_USER_ID, testAchievement);

    assertFalse(result);
    verify(userAchievementRepository, times(1)).existsByUserIdAndAchievementId(TEST_USER_ID, testAchievement.getId());
  }

  @Test
  void getAchievement_ShouldReturnAchievement_WhenExists() {
    when(achievementRepository.findByTitle("TeamCreator")).thenReturn(Optional.of(testAchievement));

    Achievement result = achievementService.getAchievement("TeamCreator");

    assertNotNull(result);
    assertEquals("TeamCreator", result.getTitle());
    verify(achievementRepository, times(1)).findByTitle("TeamCreator");
  }

  @Test
  void getAchievement_ShouldThrowException_WhenNotFound() {
    when(achievementRepository.findByTitle("NonExistent")).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> achievementService.getAchievement("NonExistent"));
  }

  @Test
  void createProgressIfNecessary_ShouldReturnExistingProgress() {
    when(achievementProgressRepository.findByUserIdAndAchievementId(TEST_USER_ID, testAchievement.getId()))
        .thenReturn(Optional.of(testProgress));

    AchievementProgress result = achievementService.createProgressIfNecessary(TEST_USER_ID, testAchievement);

    assertEquals(testProgress, result);
    verify(achievementProgressRepository, times(1)).findByUserIdAndAchievementId(TEST_USER_ID, testAchievement.getId());
  }

  @Test
  void createProgressIfNecessary_ShouldCreateNewProgress_WhenNotExists() {
    when(achievementProgressRepository.findByUserIdAndAchievementId(TEST_USER_ID, testAchievement.getId()))
        .thenReturn(Optional.empty());

    when(achievementProgressRepository.save(any(AchievementProgress.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    AchievementProgress result = achievementService.createProgressIfNecessary(TEST_USER_ID, testAchievement);

    assertNotNull(result);
    assertEquals(TEST_USER_ID, result.getUserId());
    assertEquals(testAchievement, result.getAchievement());
    assertEquals(0, result.getCurrentPoints());

    verify(achievementProgressRepository, times(1)).save(any(AchievementProgress.class));
  }

  @Test
  void increaseAchievementProgress_ShouldIncrementPoints() {
    when(achievementProgressRepository.save(any(AchievementProgress.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    AchievementProgress result = achievementService.increaseAchievementProgress(testProgress, 3);

    assertEquals(8, result.getCurrentPoints());
    verify(achievementProgressRepository, times(1)).save(testProgress);
  }

  @Test
  void giveAchievement_ShouldSaveAndPublishEvent() {
    when(userAchievementRepository.save(any(UserAchievement.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    UserAchievement result = achievementService.giveAchievement(TEST_USER_ID, testAchievement);

    assertNotNull(result);
    assertEquals(TEST_USER_ID, result.getUserId());
    assertEquals(testAchievement, result.getAchievement());

    verify(userAchievementRepository, times(1)).save(any(UserAchievement.class));
    verify(eventPublisher, times(1)).publishEvent(any(GiveAchievementEvent.class));
  }

  @Test
  void getProgress_ShouldReturnExistingProgress() {
    when(achievementProgressRepository.findByUserIdAndAchievementId(TEST_USER_ID, testAchievement.getId()))
        .thenReturn(Optional.of(testProgress));

    AchievementProgress result = achievementService.getProgress(TEST_USER_ID, testAchievement.getId());

    assertNotNull(result);
    assertEquals(testProgress, result);
  }

  @Test
  void getProgress_ShouldThrowException_WhenNotFound() {
    when(achievementProgressRepository.findByUserIdAndAchievementId(TEST_USER_ID, testAchievement.getId()))
        .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> achievementService.getProgress(TEST_USER_ID, testAchievement.getId()));
  }
}