package faang.school.achievement.cache;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AchievementCacheTest {
    private static final String REAL_CACHE_NAME = "ACHIEVEMENTS";
    @InjectMocks
    private AchievementCache achievementCache;

    @Mock
    private AchievementRepository achievementRepository;

    @Spy
    private CacheManager cacheManager = new ConcurrentMapCacheManager(REAL_CACHE_NAME);

    @Spy
    private AchievementMapper achievementMapper = Mappers.getMapper(AchievementMapper.class);

    private String anyTitle;
    private Achievement anyAchievement;

    @BeforeEach
    public void setUp() {
        anyTitle = "anyTitle";
        anyAchievement = new Achievement();
    }

    @Test
    public void fillAchievementCache_SuccessfullyFills() {
        anyAchievement.setTitle(anyTitle);

        when(achievementRepository.findAll()).thenReturn(List.of(anyAchievement));

        achievementCache.fillAchievementCache();

        verify(achievementRepository, times(1)).findAll();
        verify(cacheManager, times(1)).getCache(any(String.class));
        verify(achievementMapper, times(1)).toAchievementDto(any(Achievement.class));

    }

    @Test
    public void getByTitle_SuccessfullyReturns() {
        when(achievementRepository.findByTitle(anyTitle.toUpperCase()))
                .thenReturn(Optional.of(anyAchievement));

        achievementCache.getByTitle(anyTitle);

        verify(achievementRepository, times(1)).findByTitle(anyTitle.toUpperCase());
        verify(achievementMapper, times(1)).toAchievementDto(anyAchievement);
    }

    @Test
    public void getByTitle_NonexistentAchievement() {
        assertThrows(EntityNotFoundException.class, () -> achievementCache.getByTitle(anyTitle));

        verify(achievementRepository, times(1)).findByTitle(anyTitle.toUpperCase());
    }
}
