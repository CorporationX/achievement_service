package faang.school.achievement.service.achievement;

import faang.school.achievement.dto.achievement.AchievementProgressDto;
import faang.school.achievement.dto.achievement.UserAchievementDto;
import faang.school.achievement.mapper.achievement.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.achievement.UserAchievementPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    private static final long USER_ID_ONE = 1L;
    private static final long ACHIEVEMENT_ID_ONE = 1L;
    private static final String TITLE = "EVIL COMMENTER";
    private static final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    @InjectMocks
    private AchievementService achievementService;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Mock
    private AchievementMapper achievementMapper;
    @Mock
    private UserAchievementPublisher userAchievementPublisher;
    private Achievement achievement;
    private AchievementProgressDto achievementProgressDto;
    private AchievementProgress achievementProgress;
    private UserAchievement userAchievement;
    private UserAchievementDto userAchievementDto;


    @BeforeEach
    void setUp() {
        achievement = Achievement.builder()
                .title(TITLE)
                .id(ACHIEVEMENT_ID_ONE)
                .build();
        achievementProgressDto = AchievementProgressDto.builder()
                .userId(USER_ID_ONE)
                .build();
        achievementProgress = AchievementProgress.builder()
                .userId(USER_ID_ONE)
                .achievement(achievement)
                .currentPoints(99)
                .build();
        userAchievement = UserAchievement.builder()
                .achievement(achievement)
                .userId(USER_ID_ONE)
                .build();
        userAchievementDto = UserAchievementDto.builder()
                .achievementTitle(TITLE)
                .achievementId(ACHIEVEMENT_ID_ONE)
                .userId(USER_ID_ONE)
                .build();
    }

    @Test
    @DisplayName("When userId and achievementId passed then return AchievementProgressDto")
    public void whenUserAndAchievementValidIdsPassedThenReturnAchievementProgressDto() {
        when(achievementProgressRepository
                .findByUserIdAndAchievementId(USER_ID_ONE, ACHIEVEMENT_ID_ONE))
                .thenReturn(Optional.of(achievementProgress));
        when(achievementMapper.toAchievementProgressDto(achievementProgress))
                .thenReturn(achievementProgressDto);
        achievementService.getAchievementProgress(USER_ID_ONE, ACHIEVEMENT_ID_ONE);

        assertEquals(achievementProgress.getUserId(), achievementProgressDto.getUserId());
    }

    @Test
    @DisplayName("When user has achievement then return true")
    public void whenUserHasAchievementThenReturnTrue() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(USER_ID_ONE, ACHIEVEMENT_ID_ONE))
                .thenReturn(true);

        boolean result = achievementService.hasAchievement(USER_ID_ONE, ACHIEVEMENT_ID_ONE);

        assertTrue(result);
    }

    @Test
    @DisplayName("If no achievement create progress")
    public void whenUserHasNoAchievementThenCreateIt() {
        achievementService.createProgressIfNecessary(USER_ID_ONE, ACHIEVEMENT_ID_ONE);

        verify(achievementProgressRepository).createProgressIfNecessary(USER_ID_ONE, ACHIEVEMENT_ID_ONE);
    }

    @Test
    @DisplayName("Saves given achievement to DB and publish it to achievement_channel")
    public void whenAchievementPassedThenSaveItToDbAndPublish() {
        when(achievementMapper.toUserAchievementDto(userAchievement)).thenReturn(userAchievementDto);
        achievementService.giveAchievement(userAchievement);
        verify(userAchievementRepository).save(userAchievement);
        verify(userAchievementPublisher).publish(userAchievementDto);
    }

    @Test
    @DisplayName("When AchievementProgress passed save it")
    public void whenAchievementProgressPassedThenSaveAndReturnIt() {
        when(achievementProgressRepository.save(achievementProgress)).thenReturn(achievementProgress);
        AchievementProgress achievementSaved = achievementService.saveAchievementProgress(achievementProgress);
        assertEquals(achievementSaved, achievementProgress);
    }

    @Test
    @DisplayName("When ids passed, createProgress if needed give it plus one point, save and return it")
    public void whenIdsPassedCreateProgressIfNeededGetItFromDbThenGiveItPlusOnePointAndSaveIt(){
        when(achievementProgressRepository
                .findByUserIdAndAchievementId(USER_ID_ONE, ACHIEVEMENT_ID_ONE))
                .thenReturn(Optional.of(achievementProgress));
        when(achievementService.saveAchievementProgress(achievementProgress)).thenReturn(achievementProgress);
        AchievementProgress result = achievementService.proceedAchievementProgress(USER_ID_ONE,ACHIEVEMENT_ID_ONE);

        verify(achievementProgressRepository).createProgressIfNecessary(USER_ID_ONE,ACHIEVEMENT_ID_ONE);
        assertEquals(100, result.getCurrentPoints());
    }
}
