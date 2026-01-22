package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementCache {
    private final AchievementRepository achievementRepository;
    private final ConcurrentMap<String, Achievement> cachedAchievements = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        log.info("Loading achievements into cache...");

        achievementRepository.findAll()
                .forEach(a -> cachedAchievements.put(a.getTitle(), a));
    }

    public Achievement getOrThrow(String code) {
        return cachedAchievements.computeIfAbsent(code, this::loadByCodeFromDb);
    }

    @Transactional(readOnly = true)
    public Achievement loadByCodeFromDb(String code) {
        return achievementRepository.findByTitle(code)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Achievement with code '" + code + "' not found"
                        )
                );
    }
}