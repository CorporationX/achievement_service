package faang.school.achievement.integration.service;

import faang.school.achievement.integration.config.ConfigurationRedisTest;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.dto.AchievementRedisDto;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.AchievementCacheImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AchievementCacheIntegrationTest extends ConfigurationRedisTest {
    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private AchievementCacheImpl achievementCache;
    @Autowired
    private AchievementMapper mapper;

    @BeforeEach
    void setUp() {
        cacheManager.getCache("achievement").clear();
    }

    @Test
    void getAchievementCacheOk() {
        //given
        String title = "SENSEI";

        //when
        AchievementRedisDto result = achievementCache.getAchievementCache(title);

        AchievementRedisDto resultCached = achievementCache.getAchievementCache(title);

        //then
        assertNotNull(result);
        assertNotNull(resultCached);
        assertEquals(result, resultCached);
        assertThat(cacheManager.getCache("achievement").get(title).get()).isNotNull();
    }
}
