package faang.school.achievement.cache;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementCache {

    private final AchievementRepository achievementRepository;
    private final CacheManager cacheManager;

    @PostConstruct
    public void warmUpCache() {
        int pageSize = 5;
        int pageNumber = 0;

        while (true) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Achievement> achievementsPage = getAchievementsPage(pageable);

            if (achievementsPage == null || achievementsPage.isEmpty()) {
                break;
            }

            achievementsPage.getContent().forEach(achievement ->
                    cacheManager.getCache("achievementTitle").put(achievement.getTitle(), achievement)
            );

            log.info("Прогрет кэш для страницы {} ({} элементов)", pageNumber, achievementsPage.getContent().size());
            pageNumber++;
        }
        log.info("Прогрев кэша завершен.");
    }

    @Cacheable(value = "achievementPage", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Achievement> getAchievementsPage(Pageable pageable) {
        return achievementRepository.findAll(pageable);
    }

    @Cacheable(value = "achievementTitle")
    public Achievement get(String title) {
        return achievementRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Достижения с названием " + title + " не существует"));
    }
}
