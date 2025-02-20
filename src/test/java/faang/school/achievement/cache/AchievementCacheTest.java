package faang.school.achievement.cache;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AchievementCacheTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private AchievementCache achievementCache;


    @Test
    public void testGetAchievementFromRepo() {
        Achievement expected = Achievement.builder().title("Test").build();

        Mockito.when(achievementRepository.findByTitle("Test")).thenReturn(Optional.of(expected));
        Achievement result = achievementCache.get("Test");

        assertEquals(expected, result);
    }

    @Test
    public void testGetNotExistAchievement() {
        Mockito.when(achievementRepository.findByTitle("Test")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> achievementCache.get("Test"));
    }

    @Test
    void testWarmUpCache() {
        Achievement achievement1 = Achievement.builder().title("Test1").build();
        Achievement achievement2 = Achievement.builder().title("Test2").build();
        Achievement achievement3 = Achievement.builder().title("Test3").build();
        Achievement achievement4 = Achievement.builder().title("Test4").build();
        Achievement achievement5 = Achievement.builder().title("Test5").build();
        Achievement achievement6 = Achievement.builder().title("Test6").build();

        Mockito.when(cacheManager.getCache("achievementTitle")).thenReturn(cache);

        Pageable firstPage = PageRequest.of(0, 5);
        Mockito.when(achievementRepository.findAll(firstPage))
                .thenReturn(new PageImpl<>(List.of(achievement1, achievement2, achievement3, achievement4, achievement5)));

        Pageable secondPage = PageRequest.of(1, 5);
        Mockito.when(achievementRepository.findAll(secondPage))
                .thenReturn(new PageImpl<>(List.of(achievement6)));

        achievementCache.warmUpCache();

        Mockito.verify(cache, Mockito.times(1)).put("Test1", achievement1);
        Mockito.verify(cache, Mockito.times(1)).put("Test2", achievement2);
        Mockito.verify(cache, Mockito.times(1)).put("Test3", achievement3);
        Mockito.verify(cache, Mockito.times(1)).put("Test4", achievement4);
        Mockito.verify(cache, Mockito.times(1)).put("Test5", achievement5);
        Mockito.verify(cache, Mockito.times(1)).put("Test6", achievement6);


        Mockito.verify(cache, Mockito.times(6)).put(Mockito.anyString(), Mockito.any());
    }


}
