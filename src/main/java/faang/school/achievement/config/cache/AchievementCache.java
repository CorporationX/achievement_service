package faang.school.achievement.config.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Getter
@Slf4j
@RequiredArgsConstructor
@Validated
public class AchievementCache {

    private final AchievementRepository achievementRepository;
    private Map<String, Achievement> cache = new ConcurrentHashMap<>();

    @PostConstruct
    protected void initCache() {
        cache = achievementRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Achievement::getTitle,
                        achievement -> achievement));
    }

    public Achievement getAchievement(@NotBlank(message = "Title can't be neither blank nor null!")
                                      String achievementTitle) {
        if (cache.containsKey(achievementTitle)) {
            log.debug("Title {} is correct and exists in cache", achievementTitle);
            return cache.get(achievementTitle);
        } else {
            log.error("Such title {} does not exist!", achievementTitle);
            throw new NoSuchElementException();
        }
    }
}
