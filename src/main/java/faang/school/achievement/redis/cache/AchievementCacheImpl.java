package faang.school.achievement.redis.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AchievementCacheImpl implements AchievementCache {
    private static final int PAGE_SIZE = 10;
    private static final int CACHE_MAX_SIZE = 100;
    private static final int EXPIRED_AFTER_HOURS = 2;

    private final @Qualifier("postConstructExecutor") Executor executor;
    private final AchievementRepository repository;
    private final Cache<String, Achievement> cache = Caffeine.newBuilder()
            .expireAfterWrite(EXPIRED_AFTER_HOURS, TimeUnit.HOURS)
            .maximumSize(CACHE_MAX_SIZE)
            .build();


    @PostConstruct
    public void init() {
        int page = 0;
        int maxPage = CACHE_MAX_SIZE / PAGE_SIZE;
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        Page<Achievement> achievements;
        do {
            achievements = repository.findAll(PageRequest.of(page, PAGE_SIZE));
            if (achievements.isEmpty()) {
                break;
            }
            List<Achievement> batch = achievements.getContent();
            futures.add(CompletableFuture
                    .runAsync(
                            () -> batch.forEach(this::put),
                            executor
                    ).exceptionally(ex -> {
                        log.error("Failed to cache achievements", ex);
                        return null;
                    })
            );

            page++;
        } while (page <= maxPage);
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    public Achievement get(String title) {
        return cache.get(title, repository::findByTitleOrThrow);
    }

    public Map<String, Achievement> getAll() {
        return new HashMap<>(cache.asMap());
    }

    public void flush() {
        cache.invalidateAll();
    }

    public void put(Achievement achievement) {
        cache.put(achievement.getTitle(), achievement);
    }
}
