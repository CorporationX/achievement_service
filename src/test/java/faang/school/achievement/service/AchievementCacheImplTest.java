package faang.school.achievement.service;

import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.entity.Achievement;
import faang.school.achievement.model.dto.AchievementRedisDto;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AchievementCacheImplTest {
    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private AchievementMapper mapper;
    @Mock
    private Cache cache;

    @InjectMocks
    private AchievementCacheImpl achievementCache;

    private Achievement achievement1;
    private Achievement achievement2;
    private AchievementRedisDto dto1;
    private AchievementRedisDto dto2;

    @BeforeEach
    void setUp() {
        achievement1 = new Achievement();
        achievement2 = new Achievement();
        dto1 = new AchievementRedisDto();
        dto2 = new AchievementRedisDto();
    }

    @Test
    void addAchievementCacheOk() {
        //given
        achievement1.setTitle("Achievement 1");
        achievement2.setTitle("Achievement 2");

        when(achievementRepository.findAll()).thenReturn(List.of(achievement1, achievement2));
        when(mapper.toDto(achievement1)).thenReturn(dto1);
        when(mapper.toDto(achievement2)).thenReturn(dto2);
        when(cacheManager.getCache("achievement")).thenReturn(cache);

        //When
        achievementCache.addAchievementCache();

        //Then
        verify(achievementRepository, times(1)).findAll();
        verify(cache, times(1)).put(achievement1.getTitle(), dto1);
        verify(cache, times(1)).put(achievement2.getTitle(), dto2);
    }
}
